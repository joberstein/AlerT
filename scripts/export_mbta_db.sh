#!/bin/bash

# Note: Run this file from the scripts folder.

# Explanation:
# 1) Dump: Dump the in-memory database into a file (a sequence of 'inserts' for each table)
# 2) Copy:
# 2a) Remove the unnecessary statements from the file dump, specifically ones involving:
#  - 'android_metadata'
#  - 'sqlite_sequence'
#  - 'room_master_table'
#  - 'CREATE'
# 2b) Copy the modified file dump to external device storage, since that grants write permissions;
#     the application path may need to be created in the data folder: 'com.jesseoberstein.alert/files'.
# 3) Fetch the dump file from the connected Android device and place it in this project's assets folder.

adb shell < dump_mbta_db.txt
adb shell < copy_mbta_db.txt
adb pull //storage/emulated/0/Android/data/com.jesseoberstein.alert/files/mbta.db ../app/src/main/assets
