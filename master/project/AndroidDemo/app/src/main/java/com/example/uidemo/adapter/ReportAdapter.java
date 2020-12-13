package com.example.uidemo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.uidemo.R;
import com.example.uidemo.record.entitys.AssessmentReport;
import com.example.uidemo.test.ReportInfoActivity;

import java.util.List;

public class ReportAdapter extends BaseAdapter {
    private List<AssessmentReport> assessmentReports;
    private Context context;//上下文环境
    private int layout;
    private ProgressDialog mDialog;

    public ReportAdapter(List<AssessmentReport> assessmentReports, Context context, int layout) {
        this.assessmentReports = assessmentReports;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return assessmentReports.size();
    }

    @Override
    public Object getItem(int i) {
        return assessmentReports.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = View.inflate(context, R.layout.report_list_item, null);


        TextView tvReportId = view.findViewById(R.id.tv_report_id);
        TextView tvReportTime = view.findViewById(R.id.tv_report_time);
        TextView tvReportOverallScore = view.findViewById(R.id.tv_report_overallscore);
        Button btnReportInfo = view.findViewById(R.id.btn_report_info);

        tvReportId.setText(assessmentReports.get(i).getAssessmentReportId()+"");
        tvReportOverallScore.setText(assessmentReports.get(i).getOverallScore()+"分");
        tvReportTime.setText(assessmentReports.get(i).getTime());
        btnReportInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("id",assessmentReports.get(i).getAssessmentReportId()+"");
                intent.setClass(context, ReportInfoActivity.class);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
