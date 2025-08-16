pipeline {
    agent any

    environment {
        IMAGE_TAG       = "${env.BUILD_NUMBER}"
    }


    stages {

        stage('Build jars') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Build Docker Images') {
            steps {
                sh """
                    docker build -t account-service:${IMAGE_TAG} core/account-service
                    docker build -t blocker-service:${IMAGE_TAG} core/blocker-service
                    docker build -t cash-service:${IMAGE_TAG} core/cash-service
                    docker build -t exchange-generator-service:${IMAGE_TAG} core/exchange-generator-service
                    docker build -t exchange-service:${IMAGE_TAG} core/exchange-service
                    docker build -t front:${IMAGE_TAG} core/front
                    docker build -t notification-service:${IMAGE_TAG} core/notification-service
                    docker build -t transfer-service:${IMAGE_TAG} core/transfer-service
                """
            }
        }

        stage('Helm Deploy') {
            steps {
                dir('bankapp-umbrella') {
                    sh """
                    helm dependency update .
                    helm upgrade bankapp . --install --set image.tag=IMAGE_TAG
                    """
                }
            }
        }

    }
}