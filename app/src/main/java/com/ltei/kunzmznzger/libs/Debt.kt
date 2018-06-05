package com.ltei.kunzmznzger.libs

import com.ltei.kunzmznzger.models.User

data class Debt(val fromWho: User, val howMany : Double, val toWho: User)