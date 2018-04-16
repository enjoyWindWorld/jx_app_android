package com.jx.intelligent.video;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.result.VideoResult;
import com.squareup.picasso.Picasso;
import com.superplayer.library.utils.SuperPlayerUtils;

import java.util.List;



/**
 *
 * @author 王云
 * @time 2017-7-10
 */
public class SuperVideoAdapter extends RecyclerView.Adapter<SuperVideoAdapter.VideoViewHolder> {
    private final Context mContext;
    private List<VideoResult.DataBean> mVideDatas;;

    public SuperVideoAdapter(Context context, List<VideoResult.DataBean> mVideDatas) {
        this.mContext = context;
        this.mVideDatas = mVideDatas;
    }

    @Override
    public SuperVideoAdapter.VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(com.jx.intelligent.R.layout.video_fragment_recycle_item, viewGroup, false);
        VideoViewHolder holder = new VideoViewHolder(view);
        view.setTag(holder);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SuperVideoAdapter.VideoViewHolder holder, int position) {
        Picasso.with(mContext).load(mVideDatas.get(position).getImg()).into(holder.mVideo_img);
        holder.mVideo_title.setText(mVideDatas.get(position).getTitle());
        holder.update(position);
    }

    @Override
    public int getItemCount() {
        return mVideDatas == null ? 0 : mVideDatas.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rlayPlayerControl;
        private RelativeLayout rlayPlayer;
        private final ImageView mVideo_img;
        private final TextView mVideo_title;

        public VideoViewHolder(View itemView) {
            super(itemView);
            rlayPlayerControl = (RelativeLayout) itemView.findViewById(R.id.adapter_player_control);
            rlayPlayer = (RelativeLayout) itemView.findViewById(R.id.adapter_super_video_layout);
            mVideo_img = (ImageView) itemView.findViewById(R.id.adapter_super_video_iv_cover);
            mVideo_title = (TextView) itemView.findViewById(R.id.video_tv_title);
            if (rlayPlayer!=null){
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rlayPlayer.getLayoutParams();
                layoutParams.height = (int) (SuperPlayerUtils.getScreenWidth((Activity) mContext) * 0.5652f);//这值是网上抄来的，我设置了这个之后就没有全屏回来拉伸的效果，具体为什么我也不太清楚
                rlayPlayer.setLayoutParams(layoutParams);
            }
        }

        public void update(final int position) {
            //点击回调 播放视频
            rlayPlayerControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playclick != null)
                        playclick.onPlayclick(position, rlayPlayerControl);
                }
            });
        }
    }

    private onPlayClick playclick;

    public void setPlayClick(onPlayClick playclick) {
        this.playclick = playclick;
    }

    public interface onPlayClick {
        void onPlayclick(int position, RelativeLayout image);
    }

}
