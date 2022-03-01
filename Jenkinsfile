pipeline {
    agent any
    tools {
        maven 'maven-3.8.4' 
    }
    environment {
        DATE = new Date().format('yy.M')
        TAG = "${DATE}.${BUILD_NUMBER}"
    }
    stages {
        stage ('Build') {
            steps {
                bat 'mvn clean package'
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    docker.build("pearlcompany/poliscrypts-backimage:${TAG}")
                }
            }
        }
	    stage('Pushing Docker Image to Dockerhub') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker_hub') {
                        docker.image("pearlcompany/poliscrypts-backimage:${TAG}").push()
                        docker.image("pearlcompany/poliscrypts-backimage:${TAG}").push("latest")
                    }
                }
            }
        }
        stage('Deploy'){
            steps {
                bat "docker stop poliscrypts-backimage | true"
                bat "docker rm poliscrypts-backimage | true"
                bat "docker run --name poliscrypts-backimage -d -p 9090:9090 pearlcompany/poliscrypts-backimage:${TAG}"
            }
        }
    }
}
