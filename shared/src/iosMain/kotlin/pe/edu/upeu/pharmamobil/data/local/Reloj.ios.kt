package pe.edu.upeu.pharmamobil.data.local

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual fun segundosActuales(): Long = NSDate.date().timeIntervalSince1970.toLong()
