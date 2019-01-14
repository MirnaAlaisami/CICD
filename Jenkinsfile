node{

  //Define all variables
  def appName = 'todobackend'
  def serviceName = "${appName}-backend" 
  def imageTag = "mirna/${appName}:${env.BUILD_NUMBER}"
  
  //Checkout Code from Git
  checkout scm
  
  //Stage 1 : Build the docker image.
  stage('Build image') {
      sh("docker build -f Dockerfile-todobackend -t mirna/todobackend:${env.BUILD_NUMBER} .")
  }
  
  //Stage 2 : Push the image to docker registry
  stage('Push image to registry') {
	  withCredentials([usernamePassword( credentialsId: 'dockerhub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {

		docker.withRegistry('', 'dockerhub') {
		sh "docker login -u ${USERNAME} -p ${PASSWORD}"
		sh("docker push mirna/todobackend:${env.BUILD_NUMBER}")
			    app.push("${env.BUILD_NUMBER}")
			    app.push("latest")


		  }
		  
	  }

  
 
  
 
}
