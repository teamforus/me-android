./gradlew assembleDebugProdServer
./gradlew assembleDebugTestServer
git log -p -1  > release_notes.txt
/bin/bash telegram.sh -t 450468790:AAGdQbBgg70HuRgNSUfAPTvKZrkNpGnkRvs -c -236697194 -f release_notes.txt "Build comments"
/bin/bash telegram.sh -t 450468790:AAGdQbBgg70HuRgNSUfAPTvKZrkNpGnkRvs -c -236697194 -f app/build/outputs/apk/app-debugProdServer.apk
/bin/bash telegram.sh -t 450468790:AAGdQbBgg70HuRgNSUfAPTvKZrkNpGnkRvs -c -236697194 -f app/build/outputs/apk/app-debugTestServer.apk