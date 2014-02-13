importClass(org.slf4j.Logger);
importClass(org.slf4j.LoggerFactory);

var LOG = LoggerFactory.getLogger("resourceLogger")

function debug(message) {
	LOG.debug(message)
	if (typeof variable != 'undefined')
		LOG.debug("variable has " + variable)
	return ''
}