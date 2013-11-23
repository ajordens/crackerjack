package com.littlesquare.crackerjack.services.common.resources

import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class AbstractTransactionalDBIResource {
    protected final DBI database

    AbstractTransactionalDBIResource(DBI database) {
        this.database = database
    }

    Object transactional(Closure closure) {
        Handle handle = database.open()
        try {
            handle.begin()
            def obj = closure.call(handle)
            handle.commit()
            return obj
        } catch (Exception e) {
            handle.rollback()
            throw e
        } finally {
            handle.close()
        }
    }
}
