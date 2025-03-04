package ar.com.lpa.ldapExchanger.util;

import ar.com.lpa.ldapExchanger.model.fnObjects.P8Realm;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;

import org.apache.log4j.Logger;

public class P8ObjectSearch {
    public static IndependentObjectSet getFnObjectsFromSearch(P8Realm p8realm, Logger logger, String osName, String sqlSearch){
        ObjectStore objStore = Factory.ObjectStore.fetchInstance(p8realm.getP8domain().getDomain(), osName,null);
        SearchScope searchScope = new SearchScope(objStore);
        SearchSQL searchSQL = new SearchSQL(sqlSearch);
        logger.info("Object Search: "+ sqlSearch);
        return searchScope.fetchObjects(searchSQL, 10, null, Boolean.TRUE);
    }
}
