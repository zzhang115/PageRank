import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.ChainMapper;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzc on 7/29/17.
 */
public class UnitMultiplication {
    public static class MultiplicationReducer extends Reducer<Text, Text, Text, Text>{
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //key = 1 value = <2=1/4, 7=1/4, 8=1/4, 29=1/4, 1/6012>
            //separate transition cell from pr cell
            //multiply
            List<String> transitionCell = new ArrayList<String>();
            double prcell = 0;
            for (Text value : values) {
                if (value.toString().contains("=")) {
                    transitionCell.add(value.toString().trim());
                }
                else {
                    prcell = Double.parseDouble(value.toString().trim());
                }
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration();
        Path transitionFilePath = new Path(args[0]);
        Path prFilePath = new Path(args[1]);
        Path outputPath = new Path(args[2]);
        float beta = Float.parseFloat(args[3]);
        configuration.setFloat("beta", beta);
        Job job = Job.getInstance(configuration);
        job.setJarByClass(UnitMultiplication.class);

        ChainMapper.addMapper();

    }
}
