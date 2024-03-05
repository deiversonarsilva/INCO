package br.com.sankhya.dars.eventos;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.jape.wrapper.fluid.FluidCreateVO;

import java.math.BigDecimal;

public class eventoCriaEstruturaProduto implements EventoProgramavelJava { //TGFPRO
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
        JapeWrapper proDAO = JapeFactory.dao("Produto");//TGFPRO
        JapeWrapper estDAO = JapeFactory.dao("EstruturaLote");//TCIEST
        BigDecimal modelocodprod = BigDecimal.ONE;
        DynamicVO proVO = proDAO.findOne("CODPROD=?",vo.asBigDecimal("CODPROD"));
        if("M".equals(proVO.asString("USOPROD")) || "E".equals(proVO.asString("USOPROD"))) {
            DynamicVO estVO = estDAO.findOne("CODPROD=? AND TITULO=?", modelocodprod, "Lote Fornecedor");
            FluidCreateVO estrutura = estDAO.create();
            estrutura.set("CODPROD", vo.asBigDecimalOrZero("CODPROD"));
            estrutura.set("TITULO", estVO.asString("TITULO"));
            estrutura.set("ORDEM", estVO.asBigDecimalOrZero("ORDEM"));
            estrutura.set("TAMANHO", estVO.asBigDecimalOrZero("TAMANHO"));
            estrutura.set("TIPO", estVO.asString("TIPO"));
            estrutura.save();
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
