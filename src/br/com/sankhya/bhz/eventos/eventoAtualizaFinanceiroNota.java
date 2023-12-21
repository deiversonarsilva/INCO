package br.com.sankhya.bhz.eventos;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import com.sankhya.util.BigDecimalUtil;

import java.math.BigDecimal;

public class eventoAtualizaFinanceiroNota implements EventoProgramavelJava {//TGFFIN

    public void atualizafinanceiro(BigDecimal nunota) throws Exception {
        JapeWrapper parametroDAO = JapeFactory.dao("ParametroSistema");
        JapeWrapper finDAO = JapeFactory.dao("Financeiro");

        DynamicVO parametroVO = parametroDAO.findOne("CHAVE='TIPTITGNREST'");
        BigDecimal tipotitulo = parametroVO.asBigDecimal("INTEIRO");
        finDAO.deleteByCriteria("NUNOTA=? AND CODTIPTIT <>?",nunota,tipotitulo);
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
        JapeWrapper cabDAO = JapeFactory.dao("CabecalhoNota");
        BigDecimal nunota = BigDecimalUtil.getValueOrZero(vo.asBigDecimal("NUNOTA"));
        DynamicVO cabVO = cabDAO.findOne("NUNOTA=? AND TIPMOV='V'",nunota);
        if(cabVO != null){
            atualizafinanceiro(nunota);
        }

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
