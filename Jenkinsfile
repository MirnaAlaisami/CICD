node{

  //Define all variables
  def appName = 'todobackend'
  def imageTag = "mirna/${appName}:${env.BUILD_NUMBER}"

  //Stage 1: Checkout Code from Git
    stage('Application Code Checkout from Git') {
                sh ("checkout scm")
    }
  
  //Stage 2: Build with mvn
	stage('Build with Maven') {
		container('maven'){
			dir ('./todobackend') {
				sh ("mvn -B -DskipTests clean package")
			}
		}
	}
	
  //Stage 3: Build Docker Image	
    stage('Build Docker Image') {
		container('docker'){
			sh("docker build -f Dockerfile-todobackend -t mirna/todobackend:${env.BUILD_NUMBER} .")
		}
    }
	
  
  //Stage 4: Push the Image to a Docker Registry
    stage('Push Docker Image to Docker Registry') {
		container('docker'){
			withCredentials([[$class: 'UsernamePasswordMultiBinding',
			credentialsId: 'dockerhub',
			usernameVariable: 'USERNAME',
			passwordVariable: 'PASSWORD']]) {
				docker.withRegistry('', 'dockerhub') {
					sh "docker login -u ${USERNAME} -p ${PASSWORD}"
					sh("docker push mirna/todobackend:${env.BUILD_NUMBER}")
				}
			}
		}
    }
	
	
  //Stage 5 : Deploy Application
    stage('Deploy Application on K8s') {
		container('kubectl'){
			withKubeConfig([credentialsId: 'be4d8aa0-a081-466c-a8e9-dc0c151b3654',
		    serverUrl: 'https://35.198.139.89',
            contextName: 'gke_novatec-zweibruecken_europe-west3_nt-asca',
            clusterName: 'gke_novatec-zweibruecken_europe-west3_nt-asca']){
				sh("kubectl apply -f todobackend.yml")
			    sh("kubectl set image deployment/todobackend todobackend=${imageTag}")
				sh("kubectl create configmap postgres-config --from-literal=postgres.db.name=mydb")
			    sh("kubectl create secret generic db-security --from-literal=db.user.name=matthias --from-literal=db.user.password=password")
			    sh("kubectl apply -f postgres.yml")
			}     
		}
	}
}
