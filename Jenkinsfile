pipeline {
    agent any
    tools {
        maven 'maven-3.8.4' 
    }
    environment {
        DATE = new Date().format('yy.M')
        IMAGE_TAG = "${DATE}.${BUILD_NUMBER}"
    }
    stages {
        stage ('Build') {
            steps {
                bat 'mvn clean install'
            }
        }
        
        stage('Deploy'){
            steps {
		bat 'docker build -t ${IMAGE_TAG} .'
                bat 'docker stop poliback && docker rm poliback || true'
                bat 'docker run --name poliback -d -p 9090:9090 ${IMAGE_TAG}'
            }
        }
    }
}
