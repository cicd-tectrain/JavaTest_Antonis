//Basis Pipeline Schema anlegen

pipeline {
  agent any



    //create environment
    environment {
        INTEGRATION_BRANCH = 'intergration'
    }

    stage('Building Feature') {
      when {
              branch 'feature/*'
              beforeAgent true
            }

      //Agent overwrite and run in a docker container
      agent {
        docker {
            image 'gradle:7.5.1-jdk17-focal'
        }
      }


      steps {
        echo 'Building feature'
        sh 'ls -la'
        sh 'gradle clean build -x test'
      }
    }

    stage('Testing Feature') {
      when {
              branch 'feature/*'
              beforeAgent true
            }

    //Agent overwrite and run in a docker container
      agent {
        docker {
            image 'gradle:7.5.1-jdk17-focal'
        }
      }


      steps {
        echo 'Testing feature'
        sh 'gradle test'
      }

      //post-build
      post {
        always {
            //collect JUnit test  results
            junit 'build/test-results/**/*.xml'
        }
        success {
                    publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'build/reports/tests/test/', reportFiles: 'index.html', reportName: 'HTML Report', reportTitles: ''])
        }
      }

    }

    stage('Integrating Feature') {
      when {
        branch 'feature/*'
        beforeAgent true
      }

      steps {
              echo 'Integrating feature'
              sh 'ls -la'
              sh 'git branch -a'
              sh 'git checkout ${BRANCH_NAME}'
              sh 'git checkout ${INTEGRATION_BRANCH}'
              sh 'git merge ${BRANCH_NAME}'
              withCredentials ([
                    gitUsernamePassword(credentialsId: 'github_cicd_pat', gitToolName: 'Default')
              ]) {
                    sh 'git push origin ${INTEGRATION_BRANCH}'
              }
            }
    }

    // ====== Integration Stages ======
    stage('Build integration branch') {
        when {
            branch 'intergration'
            beforeAgent true
        }

        //Agent overwrite and run in a docker container
        agent {
            docker {
                image 'gradle:7.5.1-jdk17-focal'
            }
        }

        steps {
            echo 'Building integration'
        }
    }

    stage('Test intergration branch') {
        when {
            branch 'integration'
            beforeAgent true
        }

        steps {
            echo 'Testing integration'
        }
    }

    stage('Deploy intergration branch') {
        when {
            branch 'integration'
            beforeAgent true
        }

        steps {
            echo 'Deploying integration'
        }
    }
  }
}