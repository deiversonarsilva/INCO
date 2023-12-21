package br.com.sankhya.bhz.acoes;

import br.com.sankhya.bhz.utils.AcessoBanco;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;

public class acaoComposicao implements AcaoRotinaJava {
    @Override
    public void doAction(ContextoAcao contextoAcao) throws Exception {
        JdbcWrapper jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
        jdbc.openSession();
        AcessoBanco acessoBanco = new AcessoBanco();
        try {
            ResultSet paVO = acessoBanco.find("SELECT distinct codprodpa FROM AD_COMPOSICAO WHERE CODPRODPA BETWEEN 531 AND 550 order by codprodpa");
            BigDecimal codprodpa = BigDecimal.ZERO;
            BigDecimal seqmp = acessoBanco.findOne("SELECT MAX(SEQMP)+1 AS SEQ FROM TPRLMP WHERE IDEFX=116").getBigDecimal("SEQ");
            while (paVO.next()) {

                codprodpa = paVO.getBigDecimal("CODPRODPA");
                ResultSet mp = acessoBanco.find("SELECT * FROM AD_COMPOSICAO WHERE CODPRODPA=?",codprodpa);
                while (mp.next()){
                    BigDecimal codprodmp = mp.getBigDecimal("CODPRODMP");
                    BigDecimal qtdmistura = mp.getBigDecimal("QTDMISTURA");
                    String codvol = mp.getNString("CODVOL");
                    acessoBanco.update("INSERT INTO TPRLMP(IDEFX,SEQMP,TIPOUSOMP,TIPOCONTROLEMP,TIPOQTD,CODPRODPA,CODPRODMP,QTDMISTURA,CODVOL,VERIFICAEST,GERAREQUISICAO,CODLOCALORIG) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)",new BigDecimal(116),seqmp,"N","L","V",codprodpa,codprodmp,qtdmistura,codvol,"S","S",BigDecimal.ZERO);
                    seqmp = seqmp.add(BigDecimal.ONE);
                }

            }
        }finally {
            acessoBanco.closeSession();
        }
        jdbc.closeSession();
        contextoAcao.setMensagemRetorno("Processo Finalizado!!!");
    }
}
