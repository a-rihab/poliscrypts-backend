node {
	    // reference to maven
	    def mvnHome = tool 'maven'
	
	    // holds reference to docker image
	    def dockerImage
	 
	    def dockerImageTag = "polibackimage${env.BUILD_NUMBER}"
	    
	    stage('Clone Repo') { // for display purposes
	      // Get some code from a GitHub repository
	      git 'https://github.com/a-rihab/poliscrypts-backend.git'

	      mvnHome = tool 'maven'
	    }    
	  
	    stage('Build Project') {
	      // build project via maven
	      bat "'${mvnHome}/bin/mvn' clean install"
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