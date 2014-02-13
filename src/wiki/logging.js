importClass(org.slf4j.Logger);
importClass(org.slf4j.LoggerFactory);

var LOG = LoggerFactory.getLogger("javascriptLogger")

function debug(message){
	LOG.debug(message)
	return ''
}