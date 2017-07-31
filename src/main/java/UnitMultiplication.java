import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzc on 7/29/17.
 */
public class UnitMultiplication {
    public static class TransitionMapper extends Mapper<Object, Text, Text, Text> {
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString().trim();
            String[] flowTo = line.split("\t");
            String flow = flowTo[0].trim();
            String[] tos = flowTo[1].trim().split(",");

            if (flowTo.length ==1 || flowTo[1].trim().equals("")) {
               return;
            }
            for (String to : tos) {
                context.write(new Text(flow), new Text(to + "=" + (double)1 / tos.length));
            }
        }
    }

    public static class PRMapper extends Mapper<Object, Text, Text, Text> {
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] pr = value.toString().trim().split("\t");
            context.write(new Text(pr[0]), new Text(pr[1]));
        }
    }

    public static class MultiplicationReducer extends Reducer<Text, Text, Text, Text> {
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
            for (String cell : transitionCell) {
                String outputKey = cell.split("=")[0];
                double relation = Double.parseDouble(cell.split("=")[1]);
                String outputValue = String.valueOf(relation * prcell);
                context.write(new Text(outputKey), new Text(outputValue));
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

        ChainMapper.addMapper(job, TransitionMapper.class, Object.class, Text.class, Text.class, Text.class, configuration);

    }
}
