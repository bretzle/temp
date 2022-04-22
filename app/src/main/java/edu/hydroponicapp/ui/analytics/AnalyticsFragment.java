package edu.hydroponicapp.ui.analytics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.androidplot.xy.XYPlot;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.hydroponicapp.DbHolder;
import edu.hydroponicapp.R;
import edu.hydroponicapp.databinding.FragmentAnalyticsBinding;

public class AnalyticsFragment extends Fragment {
    private AnalyticsViewModel customizationViewModel;
    private FragmentAnalyticsBinding binding;

    TabLayout tabLayout;// = view.findViewById(R.id.tablayout);
    ViewPageAdapter viewPageAdapter;
    ViewPager2 viewPager;

    XYPlot phPlot;// = view.findViewById(R.id.plot);
    protected List<String> phValues = new ArrayList<>();
    protected List<String> timestamps = new ArrayList<>();
    protected List<String> humValues = new ArrayList<>();
    protected List<String> tempValues = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        customizationViewModel =
                new ViewModelProvider(this).get(AnalyticsViewModel.class);

        binding = FragmentAnalyticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //ph

        tabLayout = root.findViewById(R.id.tablayout);
        viewPager = root.findViewById(R.id.pager);

        FragmentManager fragmentManager =getParentFragmentManager();
        viewPageAdapter=new ViewPageAdapter(fragmentManager,getLifecycle());

        viewPager.setAdapter(viewPageAdapter);
        tabLayout.addTab(tabLayout.newTab().setText("Overview"));
        tabLayout.addTab(tabLayout.newTab().setText("ph"));
        tabLayout.addTab(tabLayout.newTab().setText("temp"));
        tabLayout.addTab(tabLayout.newTab().setText("humidity"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        return root;
    }

    private List<Number> convert(List<String> strings) {
        List<Number> returnThis = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            returnThis.add(Double.parseDouble(strings.get(i)));

        }
        return returnThis;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        viewPageAdapter = new ViewPageAdapter(this);
//        viewPager = view.findViewById(R.id.pager);
//        viewPager.setAdapter(viewPageAdapter);
//
//
//        TabLayout tabLayout = view.findViewById(R.id.tablayout);
//        new TabLayoutMediator(tabLayout, viewPager,
//                (tab, position) -> tab.setText("OBJECT " + (position + 1))
//        ).attach();

//        need to populate xy plot here
//        phPlot = view.findViewById(R.id.plot);
//        List<Number> b = convert(phValues);
//
//        XYSeries phSeries = new SimpleXYSeries(b, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "ph Series");
//        LineAndPointFormatter phFormatter = new LineAndPointFormatter(Color.RED, Color.GREEN, Color.BLUE, null);
//        phPlot.addSeries(phSeries, phFormatter);

    }

    private void initDataset() {
        DatabaseReference dbRef = DbHolder.database.getReference("SensorValues");
        Task<DataSnapshot> a = dbRef.get();

        while (!a.isComplete()) {
        }

        for (DataSnapshot snap : a.getResult().getChildren()) {
            Map<String, Object> entry = (Map<String, Object>) snap.getValue();
            String[] cur = new String[5];


            cur[0] = (String) entry.get("timestamp");
            cur[1] = String.valueOf(entry.get("ph"));
            cur[2] = (String) entry.get("temp");
            cur[3] = (String) entry.get("humidity");
            cur[4] = (String) entry.get("mineral"); //if we want to show when the mineral deposits happen

            timestamps.add(cur[0]);
            phValues.add(cur[1]);
            tempValues.add(cur[2]);
            humValues.add(cur[3]);

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
//// Instances of this class are fragments representing a single
//// object in our collection.
//class ViewPagerItem extends Fragment {
//    public static final String ARG_OBJECT = "object";
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_analytics, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Bundle args = getArguments();
//        ((TextView) view.findViewById(android.R.id.text1))
//                .setText(Integer.toString(args.getInt(ARG_OBJECT)));
//    }


