node {
	    // reference to maven
	    def mvnHome = tool 'maven-3.8.4'
	
	    // holds reference to docker image
	    def dockerImage
	 
	    def dockerImageTag = "polibackimage${env.BUILD_NUMBER}"
	    
	    stage('Clone Repo') { // for display purposes
	      // Get some code from a GitHub repository
	      git 'https://github.com/a-rihab/poliscrypts-backend.git'

	      mvnHome = tool 'maven-3.5.2'
	    }    
	  
	    stage('Build Project') {
	      // build project via maven
	      sh "'${mvnHome}/bin/mvn' clean install"
	    }
			
	    stage('Build Docker Image') {
	      // build docker image
	      dockerImage = docker.build("polibackimage:${env.BUILD_NUMBER}")
	    }
	   
	    stage('Deploy Docker Image'){
	      
	      // deploy docker image to nexus
			
		  bat "docker stop polibackimage"
		  
		  bat "docker rm polibackimage"
		  
		  bat "docker run --name polibackimage -d -p 9090:9090 polibackimage:${env.BUILD_NUMBER}"
		  
	    }

	}