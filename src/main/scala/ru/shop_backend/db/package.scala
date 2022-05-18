package ru.shop_backend

import doobie.util.fragment.Fragment

package object db {

  object SystemModel {

    case class Pagination(page: Int, pageSize: Int) {
      def sqlPagination: Fragment = {
        Fragment.const(s"LIMIT ${pageSize} OFFSET ${pageSize * page} ")
      }
    }

  }

}

