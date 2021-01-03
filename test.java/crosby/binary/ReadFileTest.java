package crosby.binary;

import crosby.binary.Osmformat.*;
import crosby.binary.file.BlockInputStream;
import crosby.binary.file.BlockReaderAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Demonstrates how to read a file. Reads sample.pbf from the resources folder
 * and prints details about it to the standard output.
 *
 * @author Michael Tandy
 */
public class ReadFileTest {

    @Test
    public void test() throws Exception {
        String expected = ("" +
                "Got header block.\n" +
                "Dense node, ID 653970877 @ 51.763603,-0.228757\n" +
                "Dense node, ID 647105170 @ 51.763591,-0.234465\n" +
                "Dense node, ID 672663476 @ 51.765749,-0.229070\n" +
                "Dense node, ID 241806356 @ 51.768945,-0.232662\n" +
                "Dense node, ID 692945017 @ 51.766185,-0.230069\n" +
                "Dense node, ID 1709246734 @ 51.766433,-0.230854\n" +
                "Dense node, ID 175685506 @ 51.765169,-0.229374\n" +
                "Dense node, ID 647105129 @ 51.769327,-0.218457\n" +
                "Dense node, ID 647105160 @ 51.768192,-0.231686\n" +
                "Dense node, ID 672663473 @ 51.765530,-0.229187\n" +
                "Dense node, ID 647105141 @ 51.773204,-0.222598\n" +
                "Dense node, ID 25365926 @ 51.766340,-0.233556\n" +
                "Dense node, ID 1685167296 @ 51.766924,-0.234783\n" +
                "Dense node, ID 677439943 @ 51.763178,-0.230230\n" +
                "Dense node, ID 1701110757 @ 51.766400,-0.228489\n" +
                "Dense node, ID 663806673 @ 51.765470,-0.229220\n" +
                "Dense node, ID 502550970 @ 51.765118,-0.233667\n" +
                "Dense node, ID 692887095 @ 51.766318,-0.229190\n" +
                "Dense node, ID 1685167376 @ 51.760411,-0.241161\n" +
                "Dense node, ID 175697821 @ 51.765000,-0.232204\n" +
                "Dense node, ID 677438877 @ 51.764126,-0.228303\n" +
                "Dense node, ID 175685111 @ 51.764882,-0.229966\n" +
                "Dense node, ID 647105131 @ 51.769022,-0.217223\n" +
                "Dense node, ID 240134267 @ 51.764217,-0.233120\n" +
                "Dense node, ID 691203111 @ 51.765755,-0.230230\n" +
                "Dense node, ID 1685167394 @ 51.761213,-0.240218\n" +
                "Dense node, ID 534873274 @ 51.763918,-0.236563\n" +
                "Dense node, ID 676945192 @ 51.765148,-0.230615\n" +
                "Dense node, ID 691203106 @ 51.764494,-0.233449\n" +
                "Dense node, ID 647105155 @ 51.769580,-0.232061\n" +
                "Dense node, ID 32950368 @ 51.769048,-0.232790\n" +
                "Dense node, ID 647105133 @ 51.769183,-0.216784\n" +
                "Dense node, ID 175683944 @ 51.763140,-0.232112\n" +
                "Dense node, ID 623540467 @ 51.765719,-0.225990\n" +
                "Dense node, ID 647225601 @ 51.762732,-0.231722\n" +
                "Dense node, ID 32953195 @ 51.761987,-0.231091\n" +
                "Dense node, ID 653970876 @ 51.763436,-0.229153\n" +
                "Dense node, ID 676945352 @ 51.765646,-0.228469\n" +
                "Dense node, ID 663806670 @ 51.765540,-0.228771\n" +
                "Dense node, ID 1709246676 @ 51.766438,-0.231121\n" +
                "Dense node, ID 647105047 @ 51.774057,-0.222895\n" +
                "Dense node, ID 175697862 @ 51.765004,-0.232747\n" +
                "Dense node, ID 647105145 @ 51.771007,-0.230355\n" +
                "Dense node, ID 647105167 @ 51.762860,-0.236278\n" +
                "Dense node, ID 1111758067 @ 51.771433,-0.216984\n" +
                "Dense node, ID 647105166 @ 51.767468,-0.234229\n" +
                "Dense node, ID 692887118 @ 51.766186,-0.228918\n" +
                "Dense node, ID 663806658 @ 51.765679,-0.228614\n" +
                "Dense node, ID 175685507 @ 51.765508,-0.229788\n" +
                "Dense node, ID 647224486 @ 51.766388,-0.228706\n" +
                "Dense node, ID 502552074 @ 51.766711,-0.229590\n" +
                "Dense node, ID 647105132 @ 51.768905,-0.216932\n" +
                "Dense node, ID 25365925 @ 51.766651,-0.233518\n" +
                "Dense node, ID 623540472 @ 51.765321,-0.225475\n" +
                "Dense node, ID 691202857 @ 51.766804,-0.231711\n" +
                "Dense node, ID 175686201 @ 51.765721,-0.228361\n" +
                "Dense node, ID 927070648 @ 51.763087,-0.232061\n" +
                "Dense node, ID 25365924 @ 51.767090,-0.233453\n" +
                "Dense node, ID 676945335 @ 51.765388,-0.228437\n" +
                "Dense node, ID 647105127 @ 51.769321,-0.219637\n" +
                "Dense node, ID 647105134 @ 51.769124,-0.216290\n" +
                "Dense node, ID 30983853 @ 51.764268,-0.233185\n" +
                "Dense node, ID 647105164 @ 51.767548,-0.233295\n" +
                "Dense node, ID 502552081 @ 51.766833,-0.233484\n" +
                "Dense node, ID 691202855 @ 51.766809,-0.231946\n" +
                "Dense node, ID 647057820 @ 51.765382,-0.226710\n" +
                "Dense node, ID 691202869 @ 51.767216,-0.231947\n" +
                "Dense node, ID 647105159 @ 51.768849,-0.232458\n" +
                "Dense node, ID 1739780291 @ 51.764890,-0.226086\n" +
                "Dense node, ID 676945267 @ 51.763905,-0.228040\n" +
                "Dense node, ID 663806664 @ 51.765444,-0.229274\n" +
                "Dense node, ID 647105143 @ 51.771399,-0.230034\n" +
                "Dense node, ID 691202858 @ 51.765928,-0.232698\n" +
                "Dense node, ID 1701110775 @ 51.766290,-0.228709\n" +
                "Dense node, ID 365548881 @ 51.763854,-0.232807\n" +
                "Dense node, ID 647224465 @ 51.765604,-0.226263\n" +
                "Dense node, ID 691202873 @ 51.766711,-0.232826\n" +
                "Dense node, ID 287659881 @ 51.766233,-0.228823\n" +
                "Dense node, ID 1685167328 @ 51.765389,-0.235803\n" +
                "Dense node, ID 1685167381 @ 51.762135,-0.238938\n" +
                "Dense node, ID 1685167371 @ 51.768683,-0.233758\n" +
                "Dense node, ID 1709246791 @ 51.765771,-0.229747\n" +
                "Dense node, ID 647105156 @ 51.769420,-0.232072\n" +
                "Dense node, ID 647105139 @ 51.773291,-0.221257\n" +
                "Dense node, ID 32953193 @ 51.763418,-0.232387\n" +
                "Dense node, ID 676945199 @ 51.765151,-0.230782\n" +
                "Dense node, ID 647105147 @ 51.770210,-0.231976\n" +
                "Dense node, ID 672628083 @ 51.764391,-0.225433\n" +
                "Dense node, ID 25365922 @ 51.768145,-0.233167\n" +
                "Dense node, ID 1709246741 @ 51.765960,-0.229886\n" +
                "Dense node, ID 647105153 @ 51.769673,-0.232265\n" +
                "Dense node, ID 30983851 @ 51.765372,-0.233546\n" +
                "Dense node, ID 691202863 @ 51.765224,-0.232225\n" +
                "Dense node, ID 691202838 @ 51.767798,-0.233387\n" +
                "Dense node, ID 175684459 @ 51.763370,-0.231564\n" +
                "Dense node, ID 1685167313 @ 51.762503,-0.238485\n" +
                "Dense node, ID 692945016 @ 51.765714,-0.230069\n" +
                "Dense node, ID 25365921 @ 51.768513,-0.232722\n" +
                "Dense node, ID 676945322 @ 51.765118,-0.229479\n" +
                "Dense node, ID 534873251 @ 51.763658,-0.236760\n" +
                "Dense node, ID 1685167341 @ 51.768171,-0.234063\n" +
                "Dense node, ID 691203110 @ 51.765769,-0.230874\n" +
                "Dense node, ID 676945292 @ 51.764506,-0.228754\n" +
                "Dense node, ID 1685167391 @ 51.761506,-0.239827\n" +
                "Dense node, ID 676945241 @ 51.763212,-0.229644\n" +
                "Dense node, ID 663806653 @ 51.765898,-0.228877\n" +
                "Dense node, ID 623624259 @ 51.764905,-0.234965\n" +
                "Dense node, ID 1685167373 @ 51.763777,-0.237235\n" +
                "Dense node, ID 676945320 @ 51.765375,-0.230143\n" +
                "Dense node, ID 240134268 @ 51.764403,-0.232382\n" +
                "Dense node, ID 676945316 @ 51.764949,-0.230532\n" +
                "Dense node, ID 623624154 @ 51.765244,-0.234365\n" +
                "Dense node, ID 647105142 @ 51.774147,-0.226321\n" +
                "Dense node, ID 1739780285 @ 51.764824,-0.226000\n" +
                "Dense node, ID 175697671 @ 51.765012,-0.233620\n" +
                "Dense node, ID 647224613 @ 51.764970,-0.229134\n" +
                "Dense node, ID 647105121 @ 51.769055,-0.221268\n" +
                "Dense node, ID 692887101 @ 51.766293,-0.228488\n" +
                "Dense node, ID 175683342 @ 51.763273,-0.229558\n" +
                "Dense node, ID 240134269 @ 51.765577,-0.230133\n" +
                "Dense node, ID 691203053 @ 51.766871,-0.230638\n" +
                "Dense node, ID 1697422651 @ 51.763725,-0.228467\n" +
                "Dense node, ID 534873285 @ 51.764110,-0.236786\n" +
                "Dense node, ID 647105148 @ 51.770131,-0.232104\n" +
                "Dense node, ID 647105165 @ 51.767482,-0.233317\n" +
                "Dense node, ID 534873185 @ 51.763403,-0.236752\n" +
                "Dense node, ID 175685104 @ 51.764391,-0.231506\n" +
                "Dense node, ID 647105163 @ 51.768079,-0.233048\n" +
                "Dense node, ID 651652536 @ 51.764591,-0.224432\n" +
                "Dense node, ID 647105115 @ 51.766990,-0.227373\n" +
                "Dense node, ID 677439944 @ 51.763332,-0.229790\n" +
                "Dense node, ID 647105162 @ 51.768232,-0.232866\n" +
                "Dense node, ID 676945319 @ 51.765218,-0.230449\n" +
                "Dense node, ID 1539682123 @ 51.769102,-0.232828\n" +
                "Dense node, ID 534873208 @ 51.763536,-0.236889\n" +
                "Dense node, ID 647105128 @ 51.769354,-0.219090\n" +
                "Dense node, ID 1739780280 @ 51.764758,-0.225914\n" +
                "Dense node, ID 175698323 @ 51.767216,-0.231110\n" +
                "Dense node, ID 676945189 @ 51.764650,-0.230926\n" +
                "Dense node, ID 1739780294 @ 51.764955,-0.224922\n" +
                "Dense node, ID 676945326 @ 51.765291,-0.229382\n" +
                "Dense node, ID 663806672 @ 51.765417,-0.229059\n" +
                "Dense node, ID 45169425 @ 51.769130,-0.233478\n" +
                "Dense node, ID 672663469 @ 51.765930,-0.229036\n" +
                "Dense node, ID 675146 @ 51.769270,-0.232860\n" +
                "Dense node, ID 691203054 @ 51.766658,-0.230273\n" +
                "Dense node, ID 1606957353 @ 51.760049,-0.241558\n" +
                "Dense node, ID 647105125 @ 51.769248,-0.220260\n" +
                "Dense node, ID 534874147 @ 51.765262,-0.235825\n" +
                "Dense node, ID 14713407 @ 51.765828,-0.227391\n" +
                "Dense node, ID 818056434 @ 51.766040,-0.233470\n" +
                "Dense node, ID 1111758069 @ 51.769198,-0.216444\n" +
                "Dense node, ID 175699187 @ 51.765663,-0.231004\n" +
                "Dense node, ID 175698155 @ 51.767389,-0.230809\n" +
                "Dense node, ID 691202861 @ 51.765516,-0.231002\n" +
                "Dense node, ID 651594517 @ 51.763745,-0.228419\n" +
                "Dense node, ID 691203051 @ 51.765901,-0.231217\n" +
                "Dense node, ID 647224485 @ 51.765127,-0.226399\n" +
                "Dense node, ID 1709246749 @ 51.765632,-0.230025\n" +
                "Dense node, ID 677440300 @ 51.762625,-0.231624\n" +
                "Dense node, ID 647105172 @ 51.764294,-0.233070\n" +
                "Dense node, ID 175686498 @ 51.765424,-0.228052\n" +
                "Dense node, ID 692944963 @ 51.764665,-0.233953\n" +
                "Dense node, ID 663806656 @ 51.765763,-0.228715\n" +
                "Dense node, ID 647105154 @ 51.769626,-0.232179\n" +
                "Dense node, ID 676945317 @ 51.765015,-0.230385\n" +
                "Dense node, ID 647105169 @ 51.763033,-0.235323\n" +
                "Dense node, ID 692945021 @ 51.766617,-0.229479\n" +
                "Dense node, ID 1709246789 @ 51.766231,-0.230173\n" +
                "Dense node, ID 175686499 @ 51.765976,-0.228635\n" +
                "Dense node, ID 691202866 @ 51.767110,-0.232955\n" +
                "Dense node, ID 1111758072 @ 51.769507,-0.216315\n" +
                "Dense node, ID 647105123 @ 51.769155,-0.220818\n" +
                "Dense node, ID 672663468 @ 51.765622,-0.228672\n" +
                "Dense node, ID 676945197 @ 51.765281,-0.230541\n" +
                "Dense node, ID 692945020 @ 51.766471,-0.229673\n" +
                "Dense node, ID 175697881 @ 51.764664,-0.232747\n" +
                "Dense node, ID 175685109 @ 51.764946,-0.230095\n" +
                "Dense node, ID 1685167304 @ 51.760787,-0.240738\n" +
                "Dense node, ID 692944951 @ 51.764943,-0.234254\n" +
                "Dense node, ID 692945019 @ 51.766225,-0.229673\n" +
                "Dense node, ID 676945334 @ 51.765467,-0.228255\n" +
                "Dense node, ID 175684463 @ 51.765445,-0.226790\n" +
                "Dense node, ID 692944957 @ 51.764651,-0.234168\n" +
                "Dense node, ID 647105144 @ 51.771332,-0.229905\n" +
                "Dense node, ID 691203055 @ 51.765928,-0.230187\n" +
                "Dense node, ID 676945331 @ 51.765589,-0.229749\n" +
                "Dense node, ID 672663474 @ 51.765638,-0.229315\n" +
                "Dense node, ID 647105146 @ 51.770283,-0.231836\n" +
                "Dense node, ID 534873171 @ 51.763005,-0.237147\n" +
                "Dense node, ID 647105157 @ 51.769307,-0.232308\n" +
                "Dense node, ID 676945327 @ 51.765347,-0.229744\n" +
                "Dense node, ID 675150 @ 51.766907,-0.229904\n" +
                "Dense node, ID 663806666 @ 51.765165,-0.228973\n" +
                "Dense node, ID 691202871 @ 51.766950,-0.232826\n" +
                "Dense node, ID 672663477 @ 51.765646,-0.228948\n" +
                "Dense node, ID 647105158 @ 51.769015,-0.232297\n" +
                "Dense node, ID 673784380 @ 51.762202,-0.231241\n" +
                "Dense node, ID 647105152 @ 51.769739,-0.232330\n" +
                "Dense node, ID 692945022 @ 51.766344,-0.228825\n" +
                "Dense node, ID 676945315 @ 51.764929,-0.230336\n" +
                "Dense node, ID 676945346 @ 51.765450,-0.228506\n" +
                "Dense node, ID 647105119 @ 51.768119,-0.223854\n" +
                "Dense node, ID 175698430 @ 51.766924,-0.231110\n" +
                "Dense node, ID 1685167387 @ 51.765901,-0.235408\n" +
                "Dense node, ID 175685910 @ 51.766003,-0.227820\n" +
                "Dense node, ID 820969139 @ 51.767836,-0.231358\n" +
                "Dense node, ID 647105102 @ 51.763883,-0.232727\n" +
                "Dense node, ID 675151 @ 51.766141,-0.228136\n" +
                "Dense node, ID 175698324 @ 51.766008,-0.231131\n" +
                "Dense node, ID 1685167282 @ 51.762958,-0.237989\n" +
                "Dense node, ID 502552090 @ 51.765557,-0.233577\n" +
                "Dense node, ID 623624155 @ 51.765449,-0.234590\n" +
                "Dense node, ID 267826070 @ 51.764017,-0.232970\n" +
                "Dense node, ID 25365930 @ 51.766791,-0.234972\n" +
                "Dense node, ID 676945195 @ 51.765156,-0.230570\n" +
                "Dense node, ID 1709246675 @ 51.766423,-0.230168\n" +
                "Dense node, ID 647105137 @ 51.774248,-0.218055\n" +
                "Dense node, ID 651652534 @ 51.764261,-0.225160\n" +
                "Dense node, ID 676945293 @ 51.764816,-0.229133\n" +
                "Dense node, ID 1692947499 @ 51.773860,-0.225851\n" +
                "Dense node, ID 623624257 @ 51.765396,-0.234075\n" +
                "Dense node, ID 175697824 @ 51.764998,-0.232032\n" +
                "Dense node, ID 672663478 @ 51.765575,-0.229104\n" +
                "Dense node, ID 1685167290 @ 51.763311,-0.237639\n" +
                "Dense node, ID 390911769 @ 51.766861,-0.229798\n" +
                "Dense node, ID 676945323 @ 51.765506,-0.229937\n" +
                "Dense node, ID 647105136 @ 51.773720,-0.217976\n" +
                "Dense node, ID 1539682039 @ 51.768036,-0.233265\n" +
                "Dense node, ID 691202860 @ 51.766247,-0.230595\n" +
                "Dense node, ID 1145410964 @ 51.769148,-0.232860\n" +
                "Dense node, ID 647105130 @ 51.769188,-0.217728\n" +
                "Dense node, ID 691203049 @ 51.766645,-0.234564\n" +
                "Dense node, ID 1539682089 @ 51.768368,-0.232938\n" +
                "Dense node, ID 175698550 @ 51.766911,-0.230809\n" +
                "Dense node, ID 623540479 @ 51.765560,-0.224961\n" +
                "Dense node, ID 677439941 @ 51.763240,-0.230472\n" +
                "Dense node, ID 25365927 @ 51.766333,-0.232681\n" +
                "Dense node, ID 647105135 @ 51.770431,-0.216476\n" +
                "Dense node, ID 30983852 @ 51.764773,-0.233577\n" +
                "Dense node, ID 647105150 @ 51.769938,-0.232265\n" +
                "Dense node, ID 623624261 @ 51.764407,-0.235985\n" +
                "Dense node, ID 647105149 @ 51.770024,-0.232212\n" +
                "Dense node, ID 677439946 @ 51.763219,-0.229690\n" +
                "Dense node, ID 691203109 @ 51.765671,-0.232912\n" +
                "Dense node, ID 647105171 @ 51.764248,-0.233242\n" +
                "Dense node, ID 1709246746 @ 51.766196,-0.230058\n" +
                "Dense node, ID 175685106 @ 51.764728,-0.230781\n" +
                "Dense node, ID 663806661 @ 51.765857,-0.228507\n" +
                "Dense node, ID 677439947 @ 51.764197,-0.228387\n" +
                "Dense node, ID 647105117 @ 51.767435,-0.226150\n" +
                "Dense node, ID 647105168 @ 51.762754,-0.235838\n" +
                "Dense node, ID 623624267 @ 51.764016,-0.233964\n" +
                "Dense node, ID 1709246737 @ 51.766423,-0.230671\n" +
                "Dense node, ID 175684462 @ 51.764271,-0.229245\n" +
                "Dense node, ID 175698551 @ 51.766539,-0.230166\n" +
                "Dense node, ID 675148 @ 51.768657,-0.232378\n" +
                "Dense node, ID 676945332 @ 51.764887,-0.229157\n" +
                "Dense node, ID 675149 @ 51.767913,-0.231459\n" +
                "Dense node, ID 692945018 @ 51.766178,-0.229758\n" +
                "Dense node, ID 623540483 @ 51.765155,-0.224456\n" +
                "Dense node, ID 676945350 @ 51.765533,-0.228712\n" +
                "Dense node, ID 175698975 @ 51.765729,-0.233577\n" +
                "Dense node, ID 175685102 @ 51.764176,-0.232069\n" +
                "Dense node, ID 676945347 @ 51.765417,-0.228572\n" +
                "Dense node, ID 534873262 @ 51.763775,-0.236889\n" +
                "Dense node, ID 25365931 @ 51.765437,-0.236066\n" +
                "Dense node, ID 672663470 @ 51.765668,-0.229614\n" +
                "Dense node, ID 647105138 @ 51.773941,-0.221418\n" +
                "Dense node, ID 647105151 @ 51.769819,-0.232340\n" +
                "Dense node, ID 32953194 @ 51.762232,-0.231263\n" +
                "Dense node, ID 1685167315 @ 51.766349,-0.235121\n" +
                "Dense node, ID 1111758071 @ 51.769058,-0.216775\n" +
                "Dense node, ID 691203098 @ 51.764324,-0.234279\n" +
                "Dense node, ID 175698553 @ 51.766253,-0.230172\n" +
                "Dense node, ID 1685167287 @ 51.767572,-0.234395\n" +
                "Dense node, ID 672663471 @ 51.765452,-0.229359\n" +
                "Dense node, ID 676945325 @ 51.765209,-0.229575\n" +
                "Dense node, ID 623624156 @ 51.765622,-0.234693\n" +
                "Dense node, ID 647105140 @ 51.773198,-0.222341\n" +
                "Dense node, ID 25365928 @ 51.766333,-0.232198\n" +
                "Dense node, ID 676945329 @ 51.765389,-0.229634\n" +
                "Dense node, ID 663806668 @ 51.765337,-0.228533\n" +
                "Dense node, ID 692944966 @ 51.764977,-0.233985\n" +
                "Dense node, ID 691203099 @ 51.764162,-0.234157\n" +
                "Dense node, ID 175685100 @ 51.764091,-0.232103\n" +
                "Dense node, ID 25365923 @ 51.767521,-0.233449\n" +
                "Dense node, ID 647105161 @ 51.767973,-0.232169\n" +
                "Dense node, ID 672663467 @ 51.765478,-0.228989\n" +
                "Dense node, ID 691202854 @ 51.766818,-0.232419\n" +
                "Way ID 158788812\n" +
                "  Nodes: 1709246789 1709246746 1709246741 1709246791 \n" +
                "  Key=value pairs: highway=footway \n" +
                "Way ID 53588781\n" +
                "  Nodes: 676945323 676945327 676945325 676945326 676945331 676945323 \n" +
                "  Key=value pairs: landuse=garages source=survey \n" +
                "Way ID 158788810\n" +
                "  Nodes: 1709246675 1709246737 1709246734 1709246676 \n" +
                "  Key=value pairs: highway=footway \n" +
                "Way ID 156255508\n" +
                "  Nodes: 45169425 1685167371 1685167341 1685167287 1685167296 1685167315 1685167387 1685167328 1685167373 1685167290 1685167282 1685167313 1685167381 1685167391 1685167394 1685167304 1685167376 1606957353 \n" +
                "  Key=value pairs: carriageway_ref=A highway=motorway lanes=3 layer=-1 lit=yes maxspeed=national name=Hatfield Tunnel oneway=yes ref=A1(M) source:maxspeed=local_knowledge tunnel=yes \n" +
                "Way ID 54932035\n" +
                "  Nodes: 691202854 691202855 691202857 \n" +
                "  Key=value pairs: highway=residential name=Jasmine Gardens source=OS_OpenData_StreetView \n" +
                "Way ID 16946553\n" +
                "  Nodes: 175697671 175697862 175697821 175697824 \n" +
                "  Key=value pairs: highway=residential name=Oak Tree Close \n" +
                "Way ID 52083876\n" +
                "  Nodes: 663806653 663806656 663806658 \n" +
                "  Key=value pairs: highway=service \n" +
                "Way ID 55081202\n" +
                "  Nodes: 692944951 692944957 692944963 692944966 692944951 \n" +
                "  Key=value pairs: leisure=common source=yahoo \n" +
                "Way ID 16946600\n" +
                "  Nodes: 175698430 175698550 691203053 691203054 175698551 1709246675 175698553 1709246789 691203055 \n" +
                "  Key=value pairs: highway=residential name=Harmony Close source=OS_OpenData_StreetView \n" +
                "Way ID 16946559\n" +
                "  Nodes: 175697862 175697881 \n" +
                "  Key=value pairs: created_by=Potlatch 0.5d highway=residential name=Oak Tree Close \n" +
                "Way ID 16945846\n" +
                "  Nodes: 175684459 175685100 175685102 175685104 676945189 175685106 676945315 175685109 175685111 175684462 \n" +
                "  Key=value pairs: highway=residential name=Stockbreach Close \n" +
                "Way ID 54932037\n" +
                "  Nodes: 175698553 691202860 \n" +
                "  Key=value pairs: highway=residential name=Harmony Close source=OS_OpenData_StreetView \n" +
                "Way ID 16946584\n" +
                "  Nodes: 175698155 175698323 175698430 1709246676 175698324 691203051 \n" +
                "  Key=value pairs: highway=residential name=The Minims source=OS_OpenData_StreetView \n" +
                "Way ID 8361329\n" +
                "  Nodes: 25365926 25365927 25365928 \n" +
                "  Key=value pairs: highway=residential name=The Paddock source=OS_OpenData_StreetView \n" +
                "Way ID 16945926\n" +
                "  Nodes: 175686498 175686201 663806661 175686499 \n" +
                "  Key=value pairs: highway=residential name=Wellfield Close \n" +
                "Way ID 52083878\n" +
                "  Nodes: 663806664 663806666 663806668 663806670 663806672 663806673 663806664 \n" +
                "  Key=value pairs: leisure=common source=yahoo \n" +
                "Way ID 3084923\n" +
                "  Nodes: 675146 1145410964 1539682123 32950368 241806356 675148 675149 820969139 175698155 675150 390911769 647224486 675151 175685910 14713407 175684463 647057820 647224485 1739780291 \n" +
                "  Key=value pairs: abutters=residential highway=secondary name=Wellfield Road ref=B197 \n" +
                "Way ID 8361331\n" +
                "  Nodes: 25365925 691203049 25365930 25365931 \n" +
                "  Key=value pairs: highway=residential name=Walsingham Close source=OS_OpenData_StreetView \n" +
                "Way ID 54932038\n" +
                "  Nodes: 175699187 691202861 \n" +
                "  Key=value pairs: highway=residential name=Middlefield source=OS_OpenData_StreetView \n" +
                "Way ID 16946620\n" +
                "  Nodes: 175698975 691203109 175699187 691203110 691203111 \n" +
                "  Key=value pairs: highway=residential name=Middlefield source=OS_OpenData_StreetView \n" +
                "Way ID 157868709\n" +
                "  Nodes: 1701110757 1701110775 \n" +
                "  Key=value pairs: bridge=yes cycleway=track highway=cycleway layer=1 name=Alban Way ncn_ref=61 railway=abandoned \n" +
                "Way ID 52083877\n" +
                "  Nodes: 663806656 663806661 \n" +
                "  Key=value pairs: highway=service \n" +
                "Way ID 53588764\n" +
                "  Nodes: 676945267 677438877 677439947 676945292 676945293 676945332 647224613 175686498 \n" +
                "  Key=value pairs: highway=footway \n" +
                "Way ID 49161822\n" +
                "  Nodes: 30983851 623624257 623624154 623624259 623624261 \n" +
                "  Key=value pairs: highway=residential name=Worcester Road \n" +
                "Way ID 49161823\n" +
                "  Nodes: 623624259 691203098 691203099 623624267 \n" +
                "  Key=value pairs: highway=residential name=Ely Close \n" +
                "Way ID 53588782\n" +
                "  Nodes: 676945327 676945329 \n" +
                "  Key=value pairs: bicycle=no highway=footway source=survey \n" +
                "Way ID 54932044\n" +
                "  Nodes: 691202869 691202855 \n" +
                "  Key=value pairs: highway=residential name=Jasmine Gardens source=OS_OpenData_StreetView \n" +
                "Way ID 49161817\n" +
                "  Nodes: 623624154 623624155 623624156 \n" +
                "  Key=value pairs: highway=residential name=Malvern Close \n" +
                "Way ID 53588780\n" +
                "  Nodes: 676945350 676945352 676945334 676945335 676945346 676945347 676945350 \n" +
                "  Key=value pairs: building=yes name=Friendship House source=survey \n" +
                "Way ID 53588749\n" +
                "  Nodes: 676945199 676945316 676945317 676945195 676945319 676945197 676945199 \n" +
                "  Key=value pairs: landuse=garages source=survey \n" +
                "Way ID 4673618\n" +
                "  Nodes: 675148 25365921 1539682089 25365922 1539682039 691202838 25365923 25365924 25365925 25365926 175698975 30983851 175697671 30983852 691203106 30983853 240134267 267826070 365548881 32953193 175683944 927070648 647225601 677440300 32953194 673784380 32953195 \n" +
                "  Key=value pairs: highway=tertiary name=Lemsford Road \n" +
                "Way ID 53638158\n" +
                "  Nodes: 677439941 677439943 677439944 677439946 676945241 175683342 653970876 653970877 1697422651 651594517 676945267 677438877 677439947 647224485 1739780291 1739780285 1739780280 672628083 651652534 651652536 1739780294 623540483 623540479 623540472 623540467 647224465 647057820 175684463 14713407 175685910 675151 692887101 647224486 647105115 647105117 647105119 647105121 647105123 647105125 647105127 647105128 647105129 647105130 647105131 647105132 1111758071 647105133 1111758069 647105134 1111758072 647105135 1111758067 647105136 647105137 647105138 647105139 647105140 647105141 647105047 1692947499 647105142 647105143 647105144 647105145 647105146 647105147 647105148 647105149 647105150 647105151 647105152 647105153 647105154 647105155 647105156 647105157 647105158 647105159 647105160 647105161 647105162 647105163 647105164 647105165 647105166 534874147 534873285 534873274 534873262 534873251 534873208 534873185 534873171 647105167 647105168 647105169 647105170 647105171 647105172 647105102 647225601 677439941 \n" +
                "  Key=value pairs: landuse=residential \n" +
                "Way ID 53588748\n" +
                "  Nodes: 676945322 676945325 676945327 676945320 676945192 676945315 \n" +
                "  Key=value pairs: bicycle=no highway=footway source=survey \n" +
                "Way ID 50772651\n" +
                "  Nodes: 175685111 676945322 175685506 647224613 \n" +
                "  Key=value pairs: highway=residential name=Town Fields \n" +
                "Way ID 158788824\n" +
                "  Nodes: 175685507 1709246749 \n" +
                "  Key=value pairs: highway=footway \n" +
                "Way ID 54932042\n" +
                "  Nodes: 691202866 691202871 691202873 \n" +
                "  Key=value pairs: highway=residential name=Jasmine Gardens source=OS_OpenData_StreetView \n" +
                "Way ID 53152061\n" +
                "  Nodes: 672663467 672663468 672663469 672663470 672663471 672663473 672663474 672663476 672663477 672663478 672663467 \n" +
                "  Key=value pairs: amenity=retirement _home building=yes name=Greenacres source:area=yahoo source:name=survey \n" +
                "Way ID 53638215\n" +
                "  Nodes: 175685506 676945329 175685507 \n" +
                "  Key=value pairs: highway=service source=survey \n" +
                "Way ID 157868710\n" +
                "  Nodes: 1701110775 287659881 692887118 1709246791 1709246749 240134269 240134268 \n" +
                "  Key=value pairs: cycleway=track highway=cycleway name=Alban Way ncn_ref=61 railway=abandoned \n" +
                "Way ID 54932036\n" +
                "  Nodes: 25365927 691202858 \n" +
                "  Key=value pairs: highway=residential name=The Paddock source=OS_OpenData_StreetView \n" +
                "Way ID 54932039\n" +
                "  Nodes: 175697821 691202863 \n" +
                "  Key=value pairs: highway=residential name=Oak Tree Close source=OS_OpenData_StreetView \n" +
                "Way ID 55081204\n" +
                "  Nodes: 692945016 692945017 692945018 692945019 692945020 692945021 692945022 692945016 \n" +
                "  Key=value pairs: leisure=common \n" +
                "Way ID 55071941\n" +
                "  Nodes: 692887118 692887101 \n" +
                "  Key=value pairs: foot=yes highway=footway \n" +
                "Way ID 157868707\n" +
                "  Nodes: 240134268 240134269 1709246749 1709246791 692887118 287659881 \n" +
                "  Key=value pairs: cycleway=track highway=cycleway name=Alban Way ncn_ref=61 \n" +
                "Got some relations to parse.\n" +
                "Complete!\n").replace("\n", System.lineSeparator());
        try (InputStream input = ReadFileTest.class.getResourceAsStream("/sample.pbf");
             StringWriter stringWriter = new StringWriter();
             PrintWriter printWriter = new PrintWriter(stringWriter)) {
            BlockReaderAdapter brad = new TestBinaryParser(printWriter);
            new BlockInputStream(input, brad).process();
            Assert.assertEquals(expected, stringWriter.toString());
        }
    }

    private static class TestBinaryParser extends BinaryParser {

        private final PrintWriter writer;

        private TestBinaryParser(PrintWriter writer) {
            this.writer = writer;
        }

        @Override
        protected void parseRelations(List<Relation> rels) {
            if (!rels.isEmpty())
                writer.println("Got some relations to parse.");
            Relation r = null;
        }

        @Override
        protected void parseDense(DenseNodes nodes) {
            long lastId = 0;
            long lastLat = 0;
            long lastLon = 0;

            for (int i = 0; i < nodes.getIdCount(); i++) {
                lastId += nodes.getId(i);
                lastLat += nodes.getLat(i);
                lastLon += nodes.getLon(i);
                writer.printf("Dense node, ID %d @ %.6f,%.6f%n",
                        lastId, parseLat(lastLat), parseLon(lastLon));
            }
        }

        @Override
        protected void parseNodes(List<Node> nodes) {
            for (Node n : nodes) {
                writer.printf("Regular node, ID %d @ %.6f,%.6f%n",
                        n.getId(), parseLat(n.getLat()), parseLon(n.getLon()));
            }
        }

        @Override
        protected void parseWays(List<Way> ways) {
            for (Way w : ways) {
                writer.println("Way ID " + w.getId());
                StringBuilder sb = new StringBuilder();
                sb.append("  Nodes: ");
                long lastRef = 0;
                for (Long ref : w.getRefsList()) {
                    lastRef += ref;
                    sb.append(lastRef).append(" ");
                }
                sb.append("\n  Key=value pairs: ");
                for (int i = 0; i < w.getKeysCount(); i++) {
                    sb.append(getStringById(w.getKeys(i))).append("=")
                            .append(getStringById(w.getVals(i))).append(" ");
                }
                writer.println(sb.toString());
            }
        }

        @Override
        protected void parse(HeaderBlock header) {
            writer.println("Got header block.");
        }

        public void complete() {
            writer.println("Complete!");
        }

    }

}
