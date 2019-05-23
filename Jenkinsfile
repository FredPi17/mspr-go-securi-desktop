node {
    // uncomment these 2 lines and edit the name 'node-4.4.7' according to what you choose in configuration
    // def nodeHome = tool name: 'node-4.4.7', type: 'jenkins.plugins.nodejs.tools.NodeJSInstallation'
    // env.PATH = "${nodeHome}/bin:${env.PATH}"

    stage('checkout') {
        checkout scm
    }

    stage('clean') {
        sh 'mvn clean'
    }

    stage('code quality') {
        sh "mvn sonar:sonar -Dsonar.host.url=http://raspberrysqlserver.ddns.net:9000 -Dsonar.login=5806ef920a7d6515531604b3527be9414b5789dc"
    }

    stage('install') {
        sh "mvn install -X"
    }

    stage('compile') {
        sh "mvn compile"
    }
}
