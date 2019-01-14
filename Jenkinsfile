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
      sh("docker push mirna/todobackend:${env.BUILD_NUMBER}")
        withDockerRegistry(registry: [credentialsId: 'dockerhub']) {
            app.push("${env.BUILD_NUMBER}")
            app.push("latest")
        }
  }
  
 
}
