package controller;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class welcome {

    public Button captureButton;
    public ImageView imageViewWebcam;
    private boolean isRunning;
    private VideoFeedTaker videoFeedTaker = new VideoFeedTaker();
    private Webcam webcam = Webcam.getDefault();
    private Image image;

    public void initialize(){
        isRunning = true;
        videoFeedTaker.start();
    }

    public void takePicture(MouseEvent mouseEvent) {
        if(isRunning){
            videoFeedTaker.interrupt();
            image = SwingFXUtils.toFXImage(webcam.getImage(), null);
        }
    }

    class VideoFeedTaker extends Thread{

        @Override
        public void run() {
            webcam.setViewSize(WebcamResolution.VGA.getSize());
            webcam.open();
            while(isRunning){
                imageViewWebcam.setImage(SwingFXUtils.toFXImage(webcam.getImage(), null));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}