cd backend
start "" powershell -WindowStyle Hidden -NoProfile -Command "Start-Sleep -Seconds 5; Start-Process 'http://localhost:8080/'"
start "" powershell -WindowStyle Hidden -NoProfile -Command "Start-Sleep -Seconds 5; Start-Process 'http://localhost:8080/h2-console'"
"D:\apache-maven-3.9.9\bin\mvn.cmd" spring-boot:run -DskipTests