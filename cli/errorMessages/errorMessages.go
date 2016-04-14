package errorMessages

import (
	"errors"
)

var ERR_AUTH_FAILED = errors.New("Authentication failed")
var ERR_UNKNOWN = errors.New("Something bad happened")
var ERR_HTTP_401 = errors.New("Wrong credentials")
var ERR_MODEL_NOT_EXIST = errors.New("Model does not exist")
var ERR_NO_VALID_ID = errors.New("Error - no valid id")
var ERR_MALFORMED_COMMAND = errors.New("Malformed command value")
var ERR_MALFORMED_NAMESPACE = errors.New("Namespace is malformed")

var ERR_NO_DEFAULT_DOWNLOAD_COMMAND = errors.New("Message - Error : No default value for download-command!")
var ERR_NO_DEFAULT_GENERATE_COMMAND = errors.New("Message - Error : No default value for download-command!")
var ERR_NO_DEFAULT_INFO_COMMAND = errors.New("Message - Error : No default value for download-command!")
var ERR_NO_DEFAULT_QUERY_COMMAND = errors.New("Message - Error : No default value for download-command!")
var ERR_NO_DEFAULT_SHARE_COMMAND = errors.New("Message - Error : No default value for share-command!")

var ERR_NO_DATATYPE = errors.New("No datatype could be defined")
var ERR_NO_ACTIVE_GENERATORS = errors.New("No active Code Generator")

var ERR_MISSING_ARGUMENTS = errors.New("Amount of arguments is to small")
var ERR_NO_VALID_COMMAND = errors.New("No valid command.")

var ERR_CONFIG = errors.New("Error in Config.")

var ERR_REPO = errors.New("Error - can not connect to Repository, please check .vortocli.yaml")
var ERR_GENERATION_ONLY_INFOMODEL = errors.New("Error - you can only generate .infomodel")
