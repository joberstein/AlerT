#!/bin/bash

# Pre-requisites:
# - Ensure an in-memory database exists under the name 'mbta' on a connected Android device.
# - The 'mbta' database should have the following tables: 'routes', 'stops', and 'endpoints'.

# Explanation:
# 1) Dump: Dump the in-memory database into a file (a sequence of 'create' and 'inserts' for each table)
# 2) Copy:
# 2a) Remove the 'android_metadata' statements from the file dump.
# 2b) Copy the modified file dump to the 'sdcard' folder, since that grants write permissions.
# 3) Fetch the dump file from the connected Android device and place it in this project's assets folder.

adb shell < dump_mbta_db.txt
adb shell < copy_mbta_db.txt
adb pull //sdcard/Apps/com.jesseoberstein.alert/mbta.db ../app/src/main/assets
