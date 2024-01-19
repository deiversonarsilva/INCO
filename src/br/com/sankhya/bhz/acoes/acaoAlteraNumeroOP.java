package br.com.sankhya.bhz.acoes;

import br.com.sankhya.bhz.utils.AcessoBanco;
import br.com.sankhya.bhz.utils.ErroUtils;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import com.sankhya.util.BigDecimalUtil;

import java.math.BigDecimal;

public class acaoAlteraNumeroOP implements AcaoRotinaJava {
    @Override
    public void doAction(ContextoAcao contextoAcao) throws Exception {
        if (contextoAcao.getLinhas().length > 1) {
            ErroUtils.disparaErro("Favor selecionar somente um registros !");
        }
        JdbcWrapper jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
        jdbc.openSession();
        JapeWrapper idiDAO = JapeFactory.dao("CabecalhoInstanciaProcesso");//TPRIPROC
        JapeWrapper ipaDAO = JapeFactory.dao("ProdutoAcabadoAProduzir");//TPRIPA
        JapeWrapper idepDAO = JapeFactory.dao("DependenciaEntreOps");//TPRIDEP
        JapeWrapper esrDAO = JapeFactory.dao("EstoqueRepositorioPA");//TPRESR
        JapeWrapper merDAO = JapeFactory.dao("MovimentacaoRepositorioPA");//TPRMER
        JapeWrapper cabDAO = JapeFactory.dao("CabecalhoNota");//TGFCAB
        String idiproc = null;
        BigDecimal idiprocant = BigDecimal.ZERO;
        Registro[] linhas = contextoAcao.getLinhas();
        for (Registro linha : linhas) {
            idiproc = String.valueOf(contextoAcao.getParam("IDIPROC"));
            idiprocant = (BigDecimal) linha.getCampo("IDIPROC");
            DynamicVO cabVO = cabDAO.findOne("IDIPROC=?",BigDecimalUtil.valueOf(idiproc));
            AcessoBanco acessoBanco = new AcessoBanco();
            try{

                acessoBanco.update("ALTER TABLE TPRIPROC DISABLE ALL TRIGGERS ");
                acessoBanco.update("ALTER TABLE TPRIDEP DISABLE CONSTRAINT FK_TPRIDEP_IDIPROCPA_TPRIPROC");
                acessoBanco.update("ALTER TABLE TPRIDEP DISABLE CONSTRAINT FK_TPRIDEP_IDIPROCPI_TPRIPROC");
                acessoBanco.update("ALTER TABLE TPRIPA DISABLE CONSTRAINT FK_TPRIPA_IDIPROC_TPRIPROC");
                acessoBanco.update("ALTER TABLE TPRIATV DISABLE CONSTRAINT FK_TPRIATV_IDIPROC_TPRIPROC");
                acessoBanco.update("ALTER TABLE TPRESR DISABLE CONSTRAINT FK_TPRESR_IDIPROC_TPRIPROC");
                acessoBanco.update("ALTER TABLE TPRMER DISABLE CONSTRAINT FK_TPRMER_IDIPROC_TPRIPROC");
                acessoBanco.update("ALTER TABLE TPRICCQ DISABLE CONSTRAINT FK_TPRICCQ_IDIPROC_TPRIPROC");
                acessoBanco.update("ALTER TABLE TGFRAM DISABLE CONSTRAINT FK_TGFRAM_TPRIPROC");
                acessoBanco.update("UPDATE TPRIPROC SET IDIPROC=? WHERE IDIPROC=?", BigDecimalUtil.valueOf(idiproc),idiprocant);


                acessoBanco.update("ALTER TABLE TPRMER DISABLE ALL TRIGGERS ");
                //acessoBanco.update("ALTER TABLE TPRMER DISABLE CONSTRAINT FK_TPRIDEP_IDIPROCPA_TPRIPROC");
                acessoBanco.update("UPDATE TPRMER SET IDIPROC=? WHERE IDIPROC=?",BigDecimalUtil.valueOf(idiproc),idiprocant);
                acessoBanco.update("ALTER TABLE TPRMER ENABLE ALL TRIGGERS ");

                acessoBanco.update("ALTER TABLE TPRIATV DISABLE ALL TRIGGERS ");
                acessoBanco.update("UPDATE TPRIATV SET IDIPROC=? WHERE IDIPROC=?", BigDecimalUtil.valueOf(idiproc),idiprocant);
                acessoBanco.update("ALTER TABLE TPRIATV ENABLE ALL TRIGGERS ");

                acessoBanco.update("ALTER TABLE TPRESR DISABLE ALL TRIGGERS ");
                acessoBanco.update("UPDATE TPRESR SET IDIPROC=? WHERE IDIPROC=?", BigDecimalUtil.valueOf(idiproc),idiprocant);
                acessoBanco.update("ALTER TABLE TPRESR ENABLE ALL TRIGGERS ");

                acessoBanco.update("ALTER TABLE TPRIDEP DISABLE ALL TRIGGERS ");
                acessoBanco.update("UPDATE TPRIDEP SET IDIPROCPA=? WHERE IDIPROCPA=?", BigDecimalUtil.valueOf(idiproc),idiprocant);
                acessoBanco.update("UPDATE TPRIDEP SET IDIPROCPI=? WHERE IDIPROCPI=?", BigDecimalUtil.valueOf(idiproc),idiprocant);
                acessoBanco.update("ALTER TABLE TPRIDEP ENABLE ALL TRIGGERS ");

                acessoBanco.update("ALTER TABLE TPRIPA DISABLE ALL TRIGGERS ");
                acessoBanco.update("UPDATE TPRIPA SET IDIPROC=? WHERE IDIPROC=?", BigDecimalUtil.valueOf(idiproc),idiprocant);
                acessoBanco.update("ALTER TABLE TPRIPA ENABLE ALL TRIGGERS ");

                acessoBanco.update("ALTER TABLE TPRICCQ DISABLE ALL TRIGGERS ");
                acessoBanco.update("UPDATE TPRICCQ SET IDIPROC=? WHERE IDIPROC=?", BigDecimalUtil.valueOf(idiproc),idiprocant);
                acessoBanco.update("ALTER TABLE TPRICCQ ENABLE ALL TRIGGERS ");

                acessoBanco.update("ALTER TABLE TGFRAM DISABLE ALL TRIGGERS ");
                acessoBanco.update("UPDATE TGFRAM SET IDIPROC=? WHERE IDIPROC=?", BigDecimalUtil.valueOf(idiproc),idiprocant);
                acessoBanco.update("ALTER TABLE TGFRAM ENABLE ALL TRIGGERS ");

                if(cabVO != null){
                    acessoBanco.update("UPDATE TGFCAB SET IDIPROC=? WHERE NUNOTA=?",BigDecimalUtil.valueOf(idiproc),cabVO.asBigDecimal("NUNOTA"));
                }
                acessoBanco.update("ALTER TABLE TPRIDEP ENABLE CONSTRAINT FK_TPRIDEP_IDIPROCPA_TPRIPROC");
                acessoBanco.update("ALTER TABLE TPRIDEP ENABLE CONSTRAINT FK_TPRIDEP_IDIPROCPI_TPRIPROC");
                acessoBanco.update("ALTER TABLE TPRIPA ENABLE CONSTRAINT FK_TPRIPA_IDIPROC_TPRIPROC");
                acessoBanco.update("ALTER TABLE TPRIATV ENABLE CONSTRAINT FK_TPRIATV_IDIPROC_TPRIPROC");
                acessoBanco.update("ALTER TABLE TPRESR ENABLE CONSTRAINT FK_TPRESR_IDIPROC_TPRIPROC");
                acessoBanco.update("ALTER TABLE TPRMER ENABLE CONSTRAINT FK_TPRMER_IDIPROC_TPRIPROC");
                acessoBanco.update("ALTER TABLE TPRICCQ ENABLE CONSTRAINT FK_TPRICCQ_IDIPROC_TPRIPROC");
                acessoBanco.update("ALTER TABLE TGFRAM ENABLE CONSTRAINT FK_TGFRAM_TPRIPROC");
                acessoBanco.update("ALTER TABLE TPRIPROC ENABLE ALL TRIGGERS ");
                acessoBanco.update("ALTER TABLE TPRMER ENABLE ALL TRIGGERS ");
                acessoBanco.update("ALTER TABLE TPRESR ENABLE ALL TRIGGERS ");
                acessoBanco.update("ALTER TABLE TPRIDEP ENABLE ALL TRIGGERS ");
                acessoBanco.update("ALTER TABLE TPRIPA ENABLE ALL TRIGGERS ");

            }finally {
                acessoBanco.closeSession();
            }
        }
        jdbc.closeSession();
        contextoAcao.setMensagemRetorno("Processo Finalizado!!!");
    }
}
