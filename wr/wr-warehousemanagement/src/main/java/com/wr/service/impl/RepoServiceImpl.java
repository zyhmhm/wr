package com.wr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wr.dao.RepoDao;
import com.wr.exe.ServiceException;
import com.wr.feignclient.UserFeignService;
import com.wr.pojo.Repo;
import com.wr.pojo.User;
import com.wr.service.RepoService;
import com.wr.util.JsonResult;
import com.wr.util.UserThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepoServiceImpl implements RepoService {
    @Autowired
    private RepoDao repoDao;
    @Autowired
    private UserFeignService userFeignService;

    /**
     * 查询仓库的基本信息
     * @return
     */
    @Override
    public JsonResult getRepoInfo() {
        Long repoId = UserThreadLocalUtil.getUser().getRepoId();
        Long userId = UserThreadLocalUtil.getUser().getId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        Repo repo = null;
        try {
            repo = repoDao.selectOne(queryWrapper);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("数据库异常，正在抢修！");
        }
        return JsonResult.ok(repo);
    }


    /**
     * 修改仓库信息
     * @param entity
     * @return
     */
    @Override
    public JsonResult doUpdateRepo(Repo entity) {
        try {
            repoDao.updateById(entity);

        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("数据库异常");
        }
        return JsonResult.ok();
    }

    /**
     * 新增仓库
     * @param entity
     * @return
     */
    @Override
    public JsonResult saveRepo(Repo entity) {
        User user = UserThreadLocalUtil.getUser();
        Long userId = user.getId();
        User user2 = new User();
        user2.setId(userId);
        entity.setUserId(userId);
        repoDao.insert(entity);
        user2.setRepoId(entity.getId());
        userFeignService.updateUser(user2);
        return JsonResult.ok();
    }

    @Override
    public JsonResult getAllRepo() {
        List<Repo> repoList = repoDao.selectList(null);
        return JsonResult.ok(repoList);
    }
}
