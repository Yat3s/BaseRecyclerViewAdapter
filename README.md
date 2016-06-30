# BaseRecyclerViewAdapter
This is a Fast and Flexible RecyclerView adapter, and is also my first library. 

You can set up your adapter easily, and you can use multi item type support easily, 
and there are many item animations and some fast api to decorate your adapter, free and enjoy it!


## DEMO
![](https://cloud.githubusercontent.com/assets/14801837/16490292/9fddf85c-3f0b-11e6-978b-7a1db93b2d3b.gif)


## Feature

- **HeaderView/ParallaxHeaderView**   
    You can easily to add header view or parallax header View.
- **MultiViewType**    
    You can ignore origin view type, and just one line to set multi view type.
- **LoadAnimation**    
    You needn't use recycler view itemAnimator and quick set item animation.
- **CustomAnimation**  
    Also you want set your custom animation is ok.
- **ItemClickListener**    
    You can set onItemClickListener easily.
- **LoadingView**  
    You can set loading view while still loading data.
- **EmptyView**    
    You can set empty view when data is empty.
- **SectionView**   //TODO
     //TODO
    

### Gradle

```groovy
dependencies {
    // TODO
    // Do not have enough time to upload this lib to maven.
    // But you can clone this project or fork this.
}
```

### Usage

```java
public class MusicAdapter extends BaseAdapter<MusicModel> {
    public MusicAdapter(Context context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder holder, MusicModel data, int position) {
        holder.setText(R.id.name_tv, data.name)
                .setText(R.id.price_tv, "$ " + data.price);
    }

    @Override
    protected int getItemViewLayoutId(int position, MusicModel musicModel) {
        if (isLargeMusicItem(position, musicModel)) {
            return R.layout.item_music_large;
        }
        return R.layout.item_music_small;
    }

    private boolean isLargeMusicItem(int position, MusicModel musicModel) {
        return 0 == position % 3 && musicModel.price < 80;
    }
}
```

You need set ```mMusicAdapter.addFirstDataSet(dataList);``` if you set first data to your adapter. and use```mMusicAdapter.addMoreDataSet(dataList);```when your addd more data to your adapter


### Features

- **Header View**   
``` java
mMusicAdapter.addHeaderViewResId(R.layout.layout_header);
```

- **Parallax Header View**
``` java
mMusicAdapter.addParallaxHeaderViewLayoutResId(R.layout.layout_header, mRecyclerView);
```

- **ItemClickListener / HeaderViewClickListener**
``` java
mMusicAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<MusicModel>() {
            @Override
            public void onClick(View view, MusicModel car, int position) {
                Toast.makeText(MainActivity.this, "Click " + position + ":" + car.name, Toast.LENGTH_SHORT).show();
            }
        });
```

```java
mMusicAdapter.setOnHeaderClickListener(new BaseAdapter.OnHeaderClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(MainActivity.this, "Click Header", Toast.LENGTH_SHORT).show();
        }
    });
```

- **ItemAnimation / CustomItemAnimation**
```
mMusicAdapter.setItemAnimation(AnimationType.SCALE);
```

```
mMusicAdapter.setCustomItemAnimator(new BaseAdapter.CustomAnimator() {
            @Override
            public Animator getAnimator(View itemView) {
                return ObjectAnimator.ofFloat(itemView, "translationX", 0, 200);
            }
        });
```

- **LoadingView / EmptyView**
``` java
mMusicAdapter.setLoadingView(yourLoadingView);
// if setShowLoadingViewIgnoreHeader(true) means when app is loading data, loading view will cover header;
mMusicAdapter.setShowLoadingViewIgnoreHeader(true);
```

``` java
mMusicAdapter.setEmptyView(yourEmptyView);
```

- **Other setting**
```
// Item Animation duration
mMusicAdapter.setItemAnimationDuration(300);  
// if true, show item animation every time when you scroll recycler view.
mMusicAdapter.setShowItemAnimationEveryTime(true);  
mMusicAdapter.setItemAnimationInterpolator(new OvershootInterpolator());
// set parallax header scroll multiplier
mMusicAdapter.setScrollMultiplier(0.5f);
mMusicAdapter.setParallaxScrollListener(new BaseAdapter.OnParallaxScrollListener() {
    @Override
    public void onParallaxScroll(float percentage, float offset, View parallax) {
        // Do some thing such as hide toolbar
    }
});
```

### Thanks
@kanytu give me some idea about parallax header
https://github.com/kanytu/android-parallax-recyclerview

