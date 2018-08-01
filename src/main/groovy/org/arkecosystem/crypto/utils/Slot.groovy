package org.arkecosystem.crypto.utils

import org.arkecosystem.crypto.configuration.Network
import java.text.SimpleDateFormat

class Slot {
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    }

    static int time() {
        (new Date().time - epoch().time) / 1000
    }

    static Date epoch() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        dateFormat.parse(Network.get().epoch)
    }
}
