package com.wr.feignclient.fb;

import com.wr.feignclient.RepoFeignService;
import com.wr.pojo.Position;
import com.wr.pojo.Repo;
import com.wr.util.JsonResult;
import org.springframework.stereotype.Component;

@Component
public class RepoFeignServiceFB implements RepoFeignService {
    @Override
    public JsonResult getRepoById(Long repoId) {
        //目前没有仓库的服务，访问必定失败，所以用此降级方法来模拟
        Repo repo = new Repo();
        if(repoId == 1l){
            repo.setId(1l)
                    .setDepositoryName("信方储存人民仓库")
                    .setCompany("信方存储")
                    .setUserId(19l).setDepositoryContacts("曾强")
                    .setDepositoryTell("12536565566").setDepositoryAdd("河南省信阳市罗山县楠杆镇人民大道1号院");
        }else{
            repo.setId(2l).setDepositoryName("腾龙一号")
                    .setCompany("东单信储")
                    .setUserId(18l).setDepositoryContacts("刘美")
                    .setDepositoryTell("17889966663").setDepositoryAdd("河南省郑州市新郑市龙湖镇双湖大道地铁站网南500米");
        }
        return JsonResult.ok(repo);
        //正常的降级操作
        /**
         * 1.先走缓存
         * 2.没有缓存返回错误
         */
    }

    @Override
    public Long doGetOrderMakingToRepo(Long userId) {
        return 1l;
    }

    @Override
    public JsonResult getPosition(Long positionId) {
        return null;
    }

    @Override
    public JsonResult doChangeByEntity(Position entity) {
        return null;
    }
}
