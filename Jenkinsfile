pipeline {

    agent any

    tools {
        maven 'M3'
    }

    stages {

        stage('Clone Repository') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Abhinesh18G/ambulance-dispatch-system.git'
            }
        }

        stage('Clean and Compile') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Package Application') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }
    }

    post {

        always {
            junit allowEmptyResults: true,
                  testResults: '**/target/surefire-reports/*.xml'
        }

        success {
            echo 'Ambulance Dispatch CI/CD Pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed. Check compilation or test errors.'
        }
    }
}
