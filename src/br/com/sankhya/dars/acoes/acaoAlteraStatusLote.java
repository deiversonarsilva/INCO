package br.com.sankhya.dars.acoes;

import br.com.sankhya.dars.utils.AcessoBanco;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.DynaformToolBarManager;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

import java.math.BigDecimal;
import java.util.Collection;

public class acaoAlteraStatusLote implements AcaoRotinaJava {
    @Override
    public void doAction(ContextoAcao contextoAcao) throws Exception {
        JdbcWrapper jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
        jdbc.openSession();

        JapeWrapper iteDAO = JapeFactory.dao("ItemNota");//TGFITE
        Registro[] linhas = contextoAcao.getLinhas();
        for (Registro linha : linhas) {
            AcessoBanco acessoBanco = new AcessoBanco();
            Collection<DynamicVO> ite = iteDAO.find("NUNOTA=? AND USOPROD = 'V'", linha.getCampo("NUNOTA"));
            for(DynamicVO iteVO :ite){
                BigDecimal nunota = iteVO.asBigDecimalOrZero("NUNOTA");
                BigDecimal codprod = iteVO.asBigDecimalOrZero("CODPROD");
                BigDecimal codlocal = iteVO.asBigDecimalOrZero("CODLOCALORIG");
                String usoprod = iteVO.asString("USOPROD");
                String controle = iteVO.asString("CONTROLE");
                try {
                    acessoBanco.update("UPDATE TGFITE SET STATUSLOTE = 'P' WHERE NUNOTA = ? AND CODPROD = ? AND USOPROD = 'V'",nunota,codprod);
                    acessoBanco.update("UPDATE TGFEST SET STATUSLOTE = 'P' WHERE CODPROD = ? AND CONTROLE = ? AND CODLOCAL = ? AND ESTOQUE >0",codprod,controle,codlocal);
                } finally {
                    acessoBanco.closeSession();
                }
            }
        }
        contextoAcao.setMensagemRetorno("Status de lote alterado!!!");
        jdbc.closeSession();
    }
}
