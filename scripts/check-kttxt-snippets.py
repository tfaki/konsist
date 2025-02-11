import subprocess
import shutil
import sys
import os

script_path = os.path.abspath(__file__)
project_root = os.path.dirname(os.path.dirname(script_path))

# convert TestData to jar. extension
command_converting_testdata_to_jar = [
    "kotlinc",
    project_root + "/lib/src/integrationTest/kotlin/com/lemonappdev/konsist/testdata/TestData.kt",
    "-include-runtime",
    "-d",
    "test.jar"
]

try:
    subprocess.run(command_converting_testdata_to_jar, check=True, text=True, capture_output=True)
except subprocess.CalledProcessError as e:
    print(f"An error occurred while running the command:\n{e.stderr}")
    error_occurred = True
else:
    error_occurred = False

# add snippet-test package
source_dir = project_root + "/lib/src/integrationTest/kotlin/com/lemonappdev/konsist/core"
destination_dir = project_root + "/build/snippet-test"

def copy_and_rename_files(source_dir, destination_dir):
    for root, dirs, files in os.walk(source_dir):
        for file in files:
            if file.endswith(".kttxt"):
                source_file_path = os.path.join(root, file)
                relative_dir = os.path.relpath(root, source_dir)
                destination_subdir = os.path.join(destination_dir, relative_dir)
                os.makedirs(destination_subdir, exist_ok=True)
                destination_file_path = os.path.join(destination_subdir, file[:-6] + ".kt")
                shutil.copy2(source_file_path, destination_file_path)

copy_and_rename_files(source_dir, destination_dir)

for root, dirs, files in os.walk(destination_dir):
    for file_name in files:
        file_path = os.path.join(root, file_name)
        with open(file_path, 'r') as file:
            file_content = file.read()

        # remove ignoring multiplatform and 1.9 kotlin version on the snippets - ticket KON-216
        if "actual" in file_content or "expect" in file_content in file_content:
            continue
        else:
            # create and run kotlinc command which verifies valid kotlin code
            snippet_command = [
                "kotlinc",
                "-cp",
                "test.jar",
                "-nowarn",
                file_path
            ]
            try:
                subprocess.run(snippet_command, check=True, text=True, capture_output=True)
            except subprocess.CalledProcessError as e:
                print(f"An error occurred while running the command:\n{e.stderr}")
                error_occurred = True

# Delete the "com" directory and its contents
try:
    shutil.rmtree(project_root + "/scripts/com")
except FileNotFoundError:
    print(f"Directory '{project_root}/scripts/com' does not exist.")
except OSError as e:
    print(f"An error occurred while deleting the directory '{project_root}/scripts/com': {e}")

# Delete the "META-INF" directory and its contents
try:
    shutil.rmtree(project_root + "/scripts/META-INF")
except FileNotFoundError:
    print(f"Directory '{project_root}/scripts/META-INF' does not exist.")
except OSError as e:
    print(f"An error occurred while deleting the directory '{project_root}/scripts/META-INF': {e}")

# Delete the "snippet-test" package and its contents
shutil.rmtree(project_root + "/build/snippet-test")

# Execute the Git command "git clean -f"
subprocess.run(["git", "clean", "-f"])

if error_occurred:
    sys.exit(1)
else:
    sys.exit(0)
