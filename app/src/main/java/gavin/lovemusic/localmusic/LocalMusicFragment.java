package gavin.lovemusic.localmusic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import gavin.lovemusic.constant.R;
import gavin.lovemusic.service.Music;

public class LocalMusicFragment extends Fragment implements LocalMusicContract.View,
        LocalRecyclerAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;

    private LocalMusicContract.Presenter mPresenter;
    private LocalRecyclerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music_local, container, false);
        mListView = (RecyclerView) rootView.findViewById(R.id.musicList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mLayoutManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout.setDistanceToTriggerSync(200);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE &&
                        mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadMusicList();
    }

    //初始化ListView视图
    @Override
    public void setMusicListView(List<Music> musicList) {
        mAdapter = new LocalRecyclerAdapter(musicList);
        mAdapter.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshMusicList(getContext());
    }

    @Override
    public void hideRefreshing() {
        if(mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(int position) {
        mPresenter.startNewMusic(mAdapter.getMusicList(), position);
    }

    @Override
    public void setPresenter(LocalMusicContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
