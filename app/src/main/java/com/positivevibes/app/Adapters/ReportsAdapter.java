package com.positivevibes.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.positivevibes.app.Models.ReportsModel.ReportsModel;
import com.positivevibes.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class ReportsAdapter  extends RecyclerView.Adapter<ReportsAdapter.MyViewHolder> {

    private Context mContext;
    private List<ReportsModel> reportsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView report_text, reply_text, report_date, reply_date;
        LinearLayout admin_reply_linear;

        public MyViewHolder(View view) {
            super(view);
            reply_text =  view.findViewById(R.id.reply_text);
            report_text = view.findViewById(R.id.reports_text);
            admin_reply_linear =  view.findViewById(R.id.admin_reply_linear);
            report_date =  view.findViewById(R.id.report_date);
            reply_date =  view.findViewById(R.id.reply_date);
        }
    }


    public ReportsAdapter(Context mContext, List<ReportsModel> reportsList) {
        this.reportsList = reportsList;
        this.mContext = mContext;

    }

    @Override
    public ReportsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reports_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.report_text.setText(reportsList.get(position).getReport_text());

        // set time Report Date //////////////////////////

        //   boom   2018-04-04 08:08:51
        //  pos    2018-04-06T09:48:40.540Z

        String tim = reportsList.get(position).getReport_date();
        String dat=tim.substring(0,10);
        String time=tim.substring(11,19);
        String fin=dat+" "+time;

        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        dff.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;

        try {
            date = dff.parse(fin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dff.setTimeZone(TimeZone.getDefault());
        String formattedDatee = dff.format(date);
        fin=formattedDatee;


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        String final_date;
        SimpleDateFormat ddf = new SimpleDateFormat("dd-MMM-yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final_date = ddf.format(calendar.getTime());

        try {
            Date commentOldDate = dateFormat.parse(fin);

            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            Date currentDate = dateFormat.parse(formattedDate);

            long diff = currentDate.getTime() - commentOldDate.getTime();

            long days = diff / (24 * 60 * 60 * 1000);
            diff -= days * (24 * 60 * 60 * 1000);

            long hours = diff / (60 * 60 * 1000);
            diff -= hours * (60 * 60 * 1000);

            long minutes = diff / (60 * 1000);
            diff -= minutes * (60 * 1000);

            long seconds = diff / 1000;


            if (days > 30) {

                holder.report_date.setText(final_date);


            } else {

                if (days >= 28) {
                    holder.report_date.setText("4 Weeks ago");

                } else {
                    if (days >= 21) {
                        holder.report_date.setText("3 Weeks ago");

                    } else {
                        if (days >= 14) {
                            holder.report_date.setText("2 Weeks ago");

                        } else {
                            if (days >= 7) {
                                holder.report_date.setText("1 Week ago");

                            } else {


                                if (days == 1) {
                                    holder.report_date.setText(days + " Day ago");
                                } else {
                                    if (days > 1) {
                                        holder.report_date.setText(days + " Days ago");

                                    } else {
                                        if (hours == 1) {
                                            holder.report_date.setText(hours + " Hour ago");
                                        } else {
                                            if (hours > 1) {
                                                holder.report_date.setText(hours + " Hours ago");

                                            } else {
                                                if (minutes == 1) {
                                                    holder.report_date.setText(minutes + " Minute ago");

                                                } else {
                                                    if (minutes > 1) {
                                                        holder.report_date.setText(minutes + " Minutes ago");

                                                    } else {
                                                        if (seconds < 0) {
                                                            holder.report_date.setText(" Now");

                                                        } else {
                                                            holder.report_date.setText(seconds + " Seconds ago");
                                                        }
                                                    }
                                                }
                                            }

                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // set Report time //////////////////////////

        if (reportsList.get(position).getAdmin_reply().equals("")){
            holder.admin_reply_linear.setVisibility(View.GONE);

        }else {
            holder.admin_reply_linear.setVisibility(View.VISIBLE);
            holder.reply_text.setText(reportsList.get(position).getAdmin_reply());
            holder.report_text.setText(reportsList.get(position).getReport_text());

            // set Reply time //////////////////////////

            //   boom   2018-04-04 08:08:51
            //  pos    2018-04-06T09:48:40.540Z

            String tim2 = reportsList.get(position).getReply_date();
            String dat2=tim2.substring(0,10);
            String time2=tim2.substring(11,19);
            String fin2=dat2+" "+time2;

            SimpleDateFormat dff2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            dff2.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date2 = null;

            try {
                date2 = dff2.parse(fin2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dff2.setTimeZone(TimeZone.getDefault());
            String formattedDatee2 = dff2.format(date2);
            fin2=formattedDatee2;


            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            String final_date2;
            SimpleDateFormat ddf2 = new SimpleDateFormat("dd-MMM-yyyy");

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date2);
            final_date2 = ddf2.format(calendar2.getTime());

            try {
                Date commentOldDate = dateFormat2.parse(fin2);

                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                Date currentDate = dateFormat2.parse(formattedDate);

                long diff = currentDate.getTime() - commentOldDate.getTime();

                long days = diff / (24 * 60 * 60 * 1000);
                diff -= days * (24 * 60 * 60 * 1000);

                long hours = diff / (60 * 60 * 1000);
                diff -= hours * (60 * 60 * 1000);

                long minutes = diff / (60 * 1000);
                diff -= minutes * (60 * 1000);

                long seconds = diff / 1000;


                if (days > 30) {

                    holder.reply_date.setText(final_date);


                } else {

                    if (days >= 28) {
                        holder.reply_date.setText("4 Weeks ago");

                    } else {
                        if (days >= 21) {
                            holder.reply_date.setText("3 Weeks ago");

                        } else {
                            if (days >= 14) {
                                holder.reply_date.setText("2 Weeks ago");

                            } else {
                                if (days >= 7) {
                                    holder.reply_date.setText("1 Week ago");

                                } else {


                                    if (days == 1) {
                                        holder.reply_date.setText(days + " Day ago");
                                    } else {
                                        if (days > 1) {
                                            holder.reply_date.setText(days + " Days ago");

                                        } else {
                                            if (hours == 1) {
                                                holder.reply_date.setText(hours + " Hour ago");
                                            } else {
                                                if (hours > 1) {
                                                    holder.reply_date.setText(hours + " Hours ago");

                                                } else {
                                                    if (minutes == 1) {
                                                        holder.reply_date.setText(minutes + " Minute ago");

                                                    } else {
                                                        if (minutes > 1) {
                                                            holder.reply_date.setText(minutes + " Minutes ago");

                                                        } else {
                                                            if (seconds < 0) {
                                                                holder.reply_date.setText(" Now");

                                                            } else {
                                                                holder.reply_date.setText(seconds + " Seconds ago");
                                                            }
                                                        }
                                                    }
                                                }

                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            // set Relpy time//////////////////////////

        }

    }

//    public void addfeed(ArrayList<ReportsModel> new_array){
//
//        int add_at_podition=reportsList.size();
//        for(int a=0;a<new_array.size(); a++){
//            reportsList.add(new_array.get(a));
//        }
//        notifyItemChanged(getItemCount());
//
//    }


    @Override
    public int getItemCount() {
        return reportsList.size();
    }
}