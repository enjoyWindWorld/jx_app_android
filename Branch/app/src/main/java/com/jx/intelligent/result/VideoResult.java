package com.jx.intelligent.result;

import java.util.List;

/**
 * Created by 王云 on 2017/6/30 0030.
 * 视频娱乐中的bean类
 */

public class VideoResult extends NormalResult {

    /**
     * data : [{"img":"http://imgmini.eastday.com/pushimg/20170622/160x90_594b217b383dd.gif","title":"乞丐管美女要钱 结果要到了内裤","video":"http://mv.eastday.com/vgaoxiao/20170619/20170619094201008580754_1_06400360.mp4"},{"img":"http://imgmini.eastday.com/pushimg/20170622/160x90_594b2caf08264.gif","title":"给鲤鱼干了两瓶二锅头以后\u2026\u2026","video":"http://mv.eastday.com/vgaoxiao/20170622/20170622081743857121528_1_06400360.mp4"},{"img":"http://imgmini.eastday.com/pushimg/20170627/205x115_5951f99c41f87.jpg","title":"这么尴尬的瞬间居然被拍下来了","video":"http://mv.eastday.com/vgaoxiao/20170317/20170317101323815139848_1_06400360.mp4"},{"img":"http://imgmini.eastday.com/pushimg/20170627/205x115_5951fd07d4ffc.jpg","title":"好坏的伴郎！这闹洞房我给满分","video":"http://mv.eastday.com/vgaoxiao/20170627/20170627110047161801053_1_06400360.mp4"},{"img":"http://imgmini.eastday.com/pushimg/20170623/160x90_594c85e4a4fec.gif","title":"急死老娘了！要摸你就赶紧的","video":"http://mv.eastday.com/vgaoxiao/20170622/20170622001118610050757_1_06400360.mp4"},{"img":"http://imgmini.eastday.com/pushimg/20170626/160x90_5950bac6ba6dc.gif","title":"要不是亲眼所见我差点就信了！","video":"http://mv.eastday.com/vpaike/20170625/20170625215823811120286_1_06400360.mp4"},{"img":"http://imgmini.eastday.com/pushimg/20170627/205x115_59520077da1ed.jpg","title":"媳妇的背叛，万万没想到系列！","video":"http://mv.eastday.com/vgaoxiao/20170627/20170627143838693522274_1_06400360.mp4"},{"img":"http://imgmini.eastday.com/pushimg/20170623/205x115_594c7f7ca968f.jpg","title":"美女正经点行不？这还结婚呢","video":"http://mv.eastday.com/vgaoxiao/20170622/20170622222152296663112_1_06400360.mp4"},{"img":"http://imgmini.eastday.com/pushimg/20170626/160x90_5950bcd33e884.gif","title":"实拍：大象发狂一头顶飞汽车","video":"http://mv.eastday.com/vzixun/20170626/20170626141723987665199_1_06400360.mp4"},{"img":"http://imgmini.eastday.com/pushimg/20170626/160x90_5950bc2ca5b02.gif","title":"相机捕捉与死神擦肩而过的一幕","video":"http://mv.eastday.com/vgaoxiao/20170623/20170623003126940042732_1_06400360.mp4"}]
     * errcode : 0
     * result : 0
     */

    private List<DataBean> data;


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * img : http://imgmini.eastday.com/pushimg/20170622/160x90_594b217b383dd.gif
         * title : 乞丐管美女要钱 结果要到了内裤
         * video : http://mv.eastday.com/vgaoxiao/20170619/20170619094201008580754_1_06400360.mp4
         */

        private String img;
        private String title;
        private String video;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }
    }
}
