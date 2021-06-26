package de.jensilus.exceptions

import java.lang.Exception

class AddressFormatException(message: String): Exception(message)
class NoConnectionException(message: String): Exception(message)