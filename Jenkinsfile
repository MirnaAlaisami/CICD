node {
	//Define all variables
	def appName = 'todobackend'
	def imageTag = "${env.REPOSITORY}/${appName}:v${env.BUILD_NUMBER}"
	def dockerFileName = 'Dockerfile-todobackend'
	def containerName = 'todobackend'
	
	//Stage 1: Checkout Code from Git
	stage('Application Code Checkout from Git') {
		checkout scm
	}
	
	//Stage 2: Test with mvn
	stage('Test') {
	
		container('docker'){
			
				sh ("docker-compose -f docker_compose_mvn_test.yml")
				sh ("mvn test")
			
		}		
        
	}
	

       
}
