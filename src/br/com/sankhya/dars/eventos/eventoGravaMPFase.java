package br.com.sankhya.dars.eventos;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.jape.wrapper.fluid.FluidCreateVO;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class eventoGravaMPFase implements EventoProgramavelJava { //TPRLMP
    JapeWrapper efxDAO = JapeFactory.dao("ElementoFluxo"); //TPREFX
    JapeWrapper faseDAO = JapeFactory.dao("AD_FASES"); //AD_FASES
    EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
    JdbcWrapper jdbc = dwfFacade.getJdbcWrapper();


    public void gravaMPFase(DynamicVO pa) throws Exception {
        DynamicVO efx = efxDAO.findOne("IDEFX=?",pa.asBigDecimalOrZero("IDEFX"));
        FluidCreateVO mp = faseDAO.create();
        mp.set("IDPROC",efx.asBigDecimalOrZero("IDPROC"));
        mp.set("CODPRODPA",pa.asBigDecimalOrZero("CODPRODPA"));
        mp.set("CONTROLEPA"," ");
        mp.set("CODPRODMP",pa.asBigDecimalOrZero("CODPRODMP"));
        mp.set("QTD",pa.asBigDecimalOrZero("QTDMISTURA"));
        mp.save();

    }
    @Override
    public void beforeInsert(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeUpdate(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeDelete(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void afterInsert(PersistenceEvent persistenceEvent) throws Exception {
        DynamicVO vo = (DynamicVO) persistenceEvent.getVo();
        gravaMPFase(vo);
    }

    @Override
    public void afterUpdate(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void afterDelete(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeCommit(TransactionContext transactionContext) throws Exception {

    }
}
