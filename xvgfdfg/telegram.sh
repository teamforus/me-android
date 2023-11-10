#!/bin/bash

VERSION="0.1"
TOKEN=""
CHATS=()
DEBUG=false

IMAGE_FILE=""
DOCUMENT_FILE=""
PARSE_MODE=""
CODE_MODE=0
ACTION=""

URL="https://api.telegram.org/bot"
CURL_OPTIONS="-s"

HAS_JQ=false
hash jq >/dev/null 2>&1 && HAS_JQ=true


function help {
	version
	echo "Usage: $0 [options] [message]"
	echo
	echo "OPTIONS are:"
	echo "    -t <TOKEN>       Telegram bot token to use. See ENVIRONMENT for more information."
	echo "    -c <CHAT_ID>     Chat to use as recipient. Can be given more than once. See ENVIRONMENT for more information."
	echo "    -f <FILE>        Sends file."
	echo "    -i <FILE>        Sends file as image. This will fail if the file isn't an actual image file."
	echo "    -M               Enables Markdown processing at telegram."
	echo "    -H               Enables HTML processing at telegram."
	echo "    -C               Sends text as monospace code. Useful when piping command outputs into this tool."
	echo "    -l               Fetch known chat_ids."
	echo
	echo "DEBUGGING OPTIONS are:"
	echo "    -v               Display lots of more or less useful information."
	echo "    -j               Pretend you don't have JQ installed."
	echo
	echo "Message can be '-', in that case STDIN will be used."
	echo
	echo "ENVIRONMENT"
	echo "    TOKEN and CHAT_ID are required. You can set them in four different ways:"
	echo "      1) globally in /etc/telegram.sh.conf"
	echo "      2) user-local in ~/.telegram.sh"
	echo "      3) via environment variables TELEGRAM_TOKEN and TELEGRAM_CHAT"
	echo "      4) via options -t and -c"
	echo "    Later methods overwrite earlier settings, so you can easily override global settings."
	echo "    Please be aware that you shuld keep your telegram token secret!"
	echo
	exit
}

function version {
	echo "telegram.sh version $VERSION"
	echo "by Fabian Schlenz"
}

function list_chats {
	log "$URL$TOKEN"
	response=`curl $CURL_OPTIONS $URL$TOKEN/getUpdates`
	log "$response"

	if [ "$HAS_JQ" = true ]; then
		echo "These are the available chats that I can find right now. The ID is the number at the front."
		echo "If there are no chats or the chat you are looking for isn't there, run this command again"
		echo "after sending a message to your bot via telegram."
		echo
		jq -r '.result | .[].message.chat | "\(.id|tostring) - \(.first_name) \(.last_name) (@\(.username))"' 2>/dev/null <<< "$response" || {
			echo "Could not parse reponse from Telegram."
			echo "Response was: $response"
			exit 1
		}
	else
		echo "You don't have jq installed. I'm afraid I can't parse the JSON from telegram without it."
		echo "So I'll have you do it. ;-)"
		echo
		echo "Please look for your chat_id in this output by yourself."
		echo 'Look for something like "chat":{"id":<CHAT_ID> and verify that first_name, last_name and'
		echo "username match your expected chat."
		echo
		echo "If there are no chats listed or the chat you are looking for isn't there, try again after"
		echo "sending a message to your bot via telegram."
		echo
		echo $response
	fi
}

function log {
	[ "$DEBUG" = true ] && echo "DEBUG: $1"
}

while getopts "t:c:i:f:MHChlvj" opt; do
	case $opt in
		t)
			TOKEN="$OPTARG"
			;;
		c)
			CHATS+=("$OPTARG")
			;;
		i)
			IMAGE_FILE="$OPTARG"
			;;
		f)
			DOCUMENT_FILE="$OPTARG"
			;;
		M)
			PARSE_MODE="Markdown"
			;;
		H)
			PARSE_MODE="HTML"
			;;
		C)
			PARSE_MODE="Markdown"
			CODE_MODE=1
			;;
		l)
			ACTION="list_chats"
			;;
		v)
			DEBUG=true
			;;
		j)
			HAS_JQ=false
			;;
		?|h)
			help
			;;
		:)
			echo "Option -$OPTARG needs an argument."
			exit 1
			;;
		\?)
			echo "Invalid option -$OPTARG"
			exit 1
			;;
	esac
done



log "TOKEN is now $TOKEN"
log "CHATS is now ${CHATS[*]}"

[ -z "$TOKEN" ] && TOKEN=$TELEGRAM_TOKEN
[ ${#CHATS[@]} -eq 0 ] && CHATS=($TELEGRAM_CHAT)

log "TOKEN is now $TOKEN"
log "CHATS is now ${CHATS[*]}"

if [ -r ~/.telegram.sh ]; then
	source ~/.telegram.sh
	[ -z "$TOKEN" ] && TOKEN=$TELEGRAM_TOKEN
	[ ${#CHATS[@]} -eq 0 ] && CHATS=($TELEGRAM_CHAT)
fi

log "TOKEN is now $TOKEN"
log "CHATS is now ${CHATS[*]}"

if [ -r /etc/telegram.sh.conf ]; then
	source /etc/telegram.sh.conf
	[ -z "$TOKEN" ] && TOKEN=$TELEGRAM_TOKEN
	[ ${#CHATS[@]} -eq 0 ] && CHATS=($TELEGRAM_CHAT)
fi

log "TOKEN is now $TOKEN"
log "CHATS is now ${CHATS[*]}"

if [ -z "$TOKEN" ]; then
	echo "No bot token was given."
	exit 1
fi

if [ ${#CHATS[@]} -eq 0 ] && [ -z "$ACTION" ]; then
	echo "No chat(s) given."
	exit 1
fi

if [ "$ACTION" = "list_chats" ]; then
	list_chats
	exit 0
fi

shift $((OPTIND - 1))
TEXT="$1"
log "Text: $TEXT"

[ "$TEXT" = "-" ] && TEXT=$(</dev/stdin)

log "Text: $TEXT"

if [ $CODE_MODE -eq 1 ]; then
	TEXT='```'$'\n'$TEXT$'\n''```'
fi

log "Text: $TEXT"

if [ -z "$TEXT" ] && [ -z "$DOCUMENT_FILE" ] && [ -z "$IMAGE_FILE" ]; then
	echo "Neither text nor image or other file given."
	exit 1
fi

if [ -n "$DOCUMENT_FILE" ] && [ -n "$IMAGE_FILE" ]; then
	echo "You can't send a file AND an image at the same time."
	exit 1
fi

if [ -n "$DOCUMENT_FILE" ]; then
	CURL_OPTIONS="$CURL_OPTIONS --form document=@$DOCUMENT_FILE"
	CURL_OPTIONS="$CURL_OPTIONS --form caption=<-"
	METHOD="sendDocument"
elif [ -n "$IMAGE_FILE" ]; then
	CURL_OPTIONS="$CURL_OPTIONS --form photo=@$IMAGE_FILE"
	CURL_OPTIONS="$CURL_OPTIONS --form caption=<-"
	METHOD="sendPhoto"
else
	CURL_OPTIONS="$CURL_OPTIONS --form text=<-"
	[ -n "$PARSE_MODE" ] && CURL_OPTIONS="$CURL_OPTIONS --form-string parse_mode=$PARSE_MODE"
	METHOD="sendMessage"
fi

for id in "${CHATS[@]}"; do
	response=`curl $CURL_OPTIONS --form-string chat_id=$id $URL$TOKEN/$METHOD <<< "$TEXT"`
	status=$?
	log "Response was: $response"
	if [ $status -ne 0 ]; then
		echo "curl reported an error. Exit code was: $status."
		echo "Response was: $response"
		echo "Quitting."
		exit $status
	fi

	if [ "$HAS_JQ" = true ]; then
		if [ "`jq -r '.ok' <<< "$response"`" != "true" ]; then
			echo "Telegram reported following error:"
			jq -r '"\(.error_code): \(.description)"' <<< "$response"
			echo "Quitting."
			exit 1
		fi
	else
		if [[ "$response" != '{"ok":true'* ]]; then
			echo "Telegram reported an error:"
			echo $response
			echo "Quitting."
			exit 1
		fi
	fi
done