package br.com.sankhya.bhz.acoes;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

import java.math.BigDecimal;
import java.util.Collection;

public class acaoAlteraNotaOP implements AcaoRotinaJava {
    @Override
    public void doAction(ContextoAcao contextoAcao) throws Exception {
        JdbcWrapper jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
        jdbc.openSession();
        JapeWrapper cabDAO = JapeFactory.dao("CabecalhoNota");//TGFCAB
        JapeWrapper iatvDAO = JapeFactory.dao("InstanciaAtividade");//TPRIATV
        JapeWrapper ropeDAO = JapeFactory.dao("RegistroOperacaoEstoque");//TPRROPE
        Collection<DynamicVO> rope = ropeDAO.find("NUNOTA IS NOT NULL");
        for(DynamicVO ropeVO : rope){
            BigDecimal idiatv = ropeVO.asBigDecimal("IDIATV");
            BigDecimal nunota = ropeVO.asBigDecimal("NUNOTA");
            DynamicVO iatv = iatvDAO.findOne("IDIATV=?",idiatv);
            BigDecimal idiproc = iatv.asBigDecimal("IDIPROC");
            DynamicVO cabVO = cabDAO.findOne("NUNOTA=?",nunota);
            cabDAO.prepareToUpdate(cabVO)
                    .set("IDIPROC",idiproc)
                    .update();
        }
        jdbc.closeSession();
        contextoAcao.setMensagemRetorno("Processo Finalizado!!!");
    }
}
