package sample;

import classes.Users;
import FaceReco.Trainer;
import FaceReco.constant.FeatureType;
import FaceReco.jama.*;
import FaceReco.training.CosineDissimilarity;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import javafx.scene.control.Alert;
import org.opencv.imgcodecs.Imgcodecs;
import utils.Utils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class IdentificationController {

    Trainer trainer = null;

    @FXML
    private Button captureButton;

    @FXML
    private ImageView originalFrame;

    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that performs the video capture
    private VideoCapture capture;
    // a flag to change the button behavior
    private boolean cameraActive;

    // face cascade classifier
    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;

    Trainer trainerface;

    @FXML
    private void initialize() {
        System.out.println("Demarrage de la page d'identification");

        recoFaceInit();

        this.capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;

        // set a fixed width for the frame
        originalFrame.setFitWidth(600);
        // preserve image ratio
        originalFrame.setPreserveRatio(true);
        // start the video capture
        this.capture.open(0);

        // is the video stream available?
        if (this.capture.isOpened()) {
            this.checkboxSelection("resources/lbpcascades/lbpcascade_frontalface.xml");
            this.cameraActive = true;

            // grab a frame every 33 ms (30 frames/sec)
            Runnable frameGrabber = new Runnable() {

                @Override
                public void run() {
                    // effectively grab and process a single frame
                    Mat frame = grabFrame();
                    // convert and show the frame
                    Image imageToShow = Utils.mat2Image(frame);
                    updateImageView(originalFrame, imageToShow);
                }
            };

            this.timer = Executors.newSingleThreadScheduledExecutor();
            this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

        } else {
            // log the error
            System.err.println("Failed to open the camera connection...");
        }

    }

    @FXML
    private void searchDataBase(ActionEvent event) {
        //Get base de donnée
        Firestore db = FirestoreClient.getFirestore();

        Mat imageAComparer = grabFrame();
        Rect[] faces = null;
        faces = this.detectFaces(imageAComparer);
        switch (faces.length) {
            case 0:
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Erreur d'authentification");
                alert1.setHeaderText(null);
                alert1.setContentText("Aucun visage detecté !");

                alert1.showAndWait();
                break;
            case 1:
                Mat visageDetecte = new Mat(imageAComparer, faces[0]);
                String tempname = "test";
                int i = 0;
                File tempFile = new File(tempname + ".pgm");
                while (tempFile.exists()) {
                    tempname = "test" + i++;
                    tempFile = new File(tempname + ".pgm");
                }

                // convert the frame in gray scale
                Imgproc.cvtColor(visageDetecte, visageDetecte, Imgproc.COLOR_BGR2GRAY);
                Size sz = new Size(512,512);
                Imgproc.resize( visageDetecte, visageDetecte, sz );

                Imgcodecs.imwrite(tempname + ".pgm", visageDetecte);

                String userRecognized = null;

                // add training data
                try {
                    userRecognized = trainerface.recognize(convertToMatrix(tempname + ".pgm"));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Users user = new Users("Unknown", "Unknown", "Unknown");
                if (userRecognized != null) {
                    user = new Users(userRecognized, userRecognized, userRecognized);
                }

                if (userRecognized != null && userRecognized.startsWith("ATTENTION")
                ) {
                    Alert alert3 = new Alert(Alert.AlertType.WARNING);
                    alert3.setTitle("Erreur d'authentification");
                    alert3.setHeight(300);
                    alert3.setWidth(500);
                    alert3.setResizable(true);
                    alert3.getDialogPane().setPrefSize(480, 220);
                    alert3.setHeaderText(null);
                    alert3.setContentText(userRecognized);

                    alert3.showAndWait();
                    break;
                }
                else{
                    Alert alert4 = new Alert(Alert.AlertType.INFORMATION);
                    alert4.setHeight(500);
                    alert4.setWidth(500);
                    alert4.setTitle("Authentification réussie");
                    alert4.setHeaderText(null);
                    alert4.setContentText("Bonjour, " +userRecognized);
                    alert4.showAndWait();
                    stopAcquisition();
                    Controller.ChangeStage(event, getClass(), user);
                    break;
                }

            default:
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Erreur d'authentification");
                alert2.setHeaderText(null);
                alert2.setContentText("Plusieurs visages détectés, veuillez présenter uniquement le visage de la personne voulant s'authentifier!");

                alert2.showAndWait();

        }
    }

    private void recoFaceInit() {
        System.out.println("Initialisation du module de reconnaissance faciale...");
        System.out.println("Ajout des données au module");

        try {
            this.trainerface = Trainer.builder()
                    .metric(new CosineDissimilarity())
                    .featureType(FeatureType.LDA)
                    .numberOfComponents(3)
                    .k(1)
                    .build();

            for (int i = 0; i < 7; i++) {
                trainerface.add(convertToMatrix("paul" + i + ".pgm"), "Paul");
                trainerface.add(convertToMatrix("guilhem" + i + ".pgm"), "Guilhem");
                trainerface.add(convertToMatrix("fred" + i + ".pgm"), "Fred");
                trainerface.add(convertToMatrix("hugo" + i + ".pgm"), "Hugo");
                trainerface.add(convertToMatrix("elsa" + i + ".pgm"), "Elsa");
            }

            System.out.println("Entrainement du module...");

            trainerface.train();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get a frame from the opened video stream (if any)
     *
     * @return the {@link Image} to show
     */
    private Mat grabFrame() {
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty()) {
                    Rect[] facesArray = this.detectFaces(frame);
                    // face detection
                    if (facesArray.length != 0) {
                        this.displayFaces(frame, facesArray);
                    }
                }

            } catch (Exception e) {
                // log the (full) error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }

    /**
     * Method for face detection and tracking
     *
     * @param frame it looks for faces in this frame
     */
    private Rect[] detectFaces(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        // convert the frame in gray scale
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // compute minimum face size (20% of the frame height, in our case)
        if (this.absoluteFaceSize == 0) {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0) {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        // detect faces
        this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

        return faces.toArray();

    }

    private void displayFaces(Mat frame, Rect[] facesArray) {
        for (int i = 0; i < facesArray.length; i++)
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
    }

    /**
     * Method for loading a classifier trained set from disk
     *
     * @param classifierPath the path on disk where a classifier trained set is located
     */
    private void checkboxSelection(String classifierPath) {
        // load the classifier(s)
        this.faceCascade.load(classifierPath);

        // now the video capture can start
        this.captureButton.setDisable(false);
    }

    /**
     * Stop the acquisition from the camera and release all the resources
     */
    private void stopAcquisition() {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
                return ;
            }
        }

        if (this.capture.isOpened()) {
            // release the camera
            this.capture.release();
        }
    }

    /**
     * Update the {@link ImageView} in the JavaFX main thread
     *
     * @param view  the {@link ImageView} to update
     * @param image the {@link Image} to show
     */
    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    private Matrix convertToMatrix(String fileAddress) throws IOException {
        File file = new File(fileAddress);
        return vectorize(Utils.convertPGMtoMatrix(file.getAbsolutePath()));
    }

    //Convert a m by n matrix into a m*n by 1 matrix
    static Matrix vectorize(Matrix input) {
        int m = input.getRowDimension();
        int n = input.getColumnDimension();

        Matrix result = new Matrix(m * n, 1);
        for (int p = 0; p < n; p++) {
            for (int q = 0; q < m; q++) {
                result.set(p * m + q, 0, input.get(q, p));
            }
        }
        return result;

    }
}
