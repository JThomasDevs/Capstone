import subprocess
import sys
import signal
import os

# Paths to the Java API server JAR and Streamlit app
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
JAR_PATH = os.path.join(BASE_DIR, "target", "rescue-animal-system-1.0-SNAPSHOT.jar")
STREAMLIT_APP_PATH = os.path.join(BASE_DIR, "src", "view", "app.py")

# Command to start the Java API server
JAVA_CMD = [
    "java", "-cp", JAR_PATH, "com.rescueanimals.controllers.RescueServer"
]

# Command to start the Streamlit app
STREAMLIT_CMD = [
    "streamlit", "run", STREAMLIT_APP_PATH
]

# Start both processes
java_proc = subprocess.Popen(JAVA_CMD)
streamlit_proc = subprocess.Popen(STREAMLIT_CMD)

def cleanup(signum, frame):
    """
    Cleans up both processes when the program is terminated.
    """
    print("Shutting down both processes...")
    java_proc.terminate()
    streamlit_proc.terminate()
    java_proc.wait()
    streamlit_proc.wait()
    sys.exit(0)

# Handle Ctrl+C and termination signals
signal.signal(signal.SIGINT, cleanup)
signal.signal(signal.SIGTERM, cleanup)

try:
    # Wait for both processes to finish
    java_proc.wait()
    streamlit_proc.wait()
except KeyboardInterrupt:
    cleanup(None, None) 