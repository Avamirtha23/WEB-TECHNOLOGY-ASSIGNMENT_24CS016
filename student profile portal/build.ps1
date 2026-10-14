# ====================================================================
#  PowerShell Build & Deployment Script for Student Profile Portal
#  U21CS504 Web Technology Lab - Assignment 3
# ====================================================================

$ProjectDir = $PSScriptRoot
$TomcatDir = "C:\Users\amirt\Downloads\apache-tomcat-10.1.61-windows-x64\apache-tomcat-10.1.61"
$ServletJar = "$TomcatDir\lib\servlet-api.jar"
$MySqlJar = "$ProjectDir\WebContent\WEB-INF\lib\mysql-connector-j-8.3.0.jar"
$ClassesDir = "$ProjectDir\WebContent\WEB-INF\classes"
$WarPath = "$ProjectDir\StudentPortal.war"
$JarExe = "C:\Program Files\Java\jdk-21.0.12\bin\jar.exe"

Write-Host "--- [1/3] Checking output directories ---" -ForegroundColor Cyan
if (!(Test-Path $ClassesDir)) {
    New-Item -ItemType Directory -Path $ClassesDir -Force | Out-Null
}

Write-Host "--- [2/3] Compiling Java source files ---" -ForegroundColor Cyan
$Sources = Get-ChildItem -Path "$ProjectDir\src" -Filter "*.java" -Recurse | Select-Object -ExpandProperty FullName
javac -cp "$ServletJar;$MySqlJar" -d "$ClassesDir" $Sources

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Java compilation failed!" -ForegroundColor Red
    exit $LASTEXITCODE
}
Write-Host "[SUCCESS] Compiled $($Sources.Count) source files successfully." -ForegroundColor Green

Write-Host "--- [3/3] Packaging WAR file ---" -ForegroundColor Cyan
& $JarExe -cvf "$WarPath" -C "$ProjectDir\WebContent" . | Out-Null
Write-Host "[SUCCESS] Generated WAR: $WarPath" -ForegroundColor Green

if (Test-Path "$TomcatDir\webapps") {
    Copy-Item "$WarPath" -Destination "$TomcatDir\webapps\StudentPortal.war" -Force
    $ExplodedDir = "$TomcatDir\webapps\StudentPortal"
    Copy-Item -Path "$ProjectDir\WebContent\*" -Destination $ExplodedDir -Recurse -Force
    Write-Host "[SUCCESS] Deployed to Tomcat webapps: $ExplodedDir" -ForegroundColor Green
}

Write-Host "`nAll steps complete! Access portal at: http://localhost:8080/StudentPortal/`n" -ForegroundColor Yellow
