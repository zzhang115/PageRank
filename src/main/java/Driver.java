import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.hash.Hash;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by zzc on 7/29/17.
 */
public class Driver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // first task
        Path transitionFilePath = new Path(args[0]);
        Path prFilePath = new Path(args[1]);
        Path outputPath = new Path(args[2]);
        float beta = Float.parseFloat(args[3]);

        // mapper1 reducer1
        Configuration configuration1 = new Configuration();
        configuration1.setFloat("beta", beta);
        Job job1 = Job.getInstance(configuration1);
        job1.setJarByClass(UnitMultiplication.class);

        File outPutFile1 = new File(args[2]);
        if (outPutFile1.exists()) {
            System.out.println("Output1 directory already exits!\nDelete previous output directory.");
            FileUtils.deleteDirectory(outPutFile1);
        }

        ChainMapper.addMapper(job1, UnitMultiplication.TransitionMapper.class, Object.class, Text.class, Text.class, Text.class, configuration1);
        ChainMapper.addMapper(job1, UnitMultiplication.PRMapper.class, Object.class, Text.class, Text.class, Text.class, configuration1);

        job1.setReducerClass(UnitMultiplication.MultiplicationReducer.class);

        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(Text.class);

        MultipleInputs.addInputPath(job1, transitionFilePath, TextInputFormat.class, UnitMultiplication.TransitionMapper.class);
        MultipleInputs.addInputPath(job1, prFilePath, TextInputFormat.class, UnitMultiplication.PRMapper.class);

        FileOutputFormat.setOutputPath(job1, outputPath);
        job1.waitForCompletion(true);

        // second task
        Path unitInputPath = new Path(args[4]);
        Path unitOutputPath= new Path(args[5]);

        File outPutFile2 = new File(args[5]);
        if (outPutFile2.exists()) {
            System.out.println("Output2 directory already exists\nDelete previous output directory.");
            FileUtils.deleteDirectory(outPutFile2);
        }
        // mapeper2 reducer2
        Configuration configuration2 = new Configuration();
        Job job2 = Job.getInstance(configuration1);
        job2.setJarByClass(UnitSum.class);

        job2.setMapperClass(UnitSum.SumMapper.class);
        job2.setReducerClass(UnitSum.SumReducer.class);

        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.addInputPath(job2, unitInputPath);
        FileOutputFormat.setOutputPath(job2, unitOutputPath);
        job2.waitForCompletion(true);
    }
}
