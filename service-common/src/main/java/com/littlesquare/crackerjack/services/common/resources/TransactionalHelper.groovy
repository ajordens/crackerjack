package com.littlesquare.crackerjack.services.common.resources

import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle

/**
 * @author Adam Jordens (adam@jordens.org)
 */
@SuppressWarnings(['CatchException'])
public class TransactionalHelper {
    protected final DBI database

    public TransactionalHelper(DBI database) {
        this.database = database
    }

    public Object transactional(Closure closure) {
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
