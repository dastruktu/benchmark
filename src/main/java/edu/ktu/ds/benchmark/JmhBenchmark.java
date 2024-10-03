package edu.ktu.ds.benchmark;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

// Measures the average execution time of benchmark methods (arrayListGet()
// and linkedListGet()). See JMH sample JMHSample_02_BenchmarkModes.java for details
@BenchmarkMode(Mode.AverageTime)
// Benchmark class also saves benchmark state (arrayList, linkedList ir indexes)
// that is used by all threads that run the benchmark. More sophisticated
// benchmarks where measured methods change benchmark state, i.e. not only reads,
// but also write to it, use separate classes and/or various Scopes (Scope.Benchmark
// or Scope.Thread). See JMH samples JMHSample_03_States.java and
// JMHSample_04_DefaultState.java for details
@State(Scope.Benchmark)
// The measured time wil lbe given in microseconds. See JMH sample
// JMHSample_02_BenchmarkModes.java for details
@OutputTimeUnit(TimeUnit.MICROSECONDS)
// Benchmarking starts with virtual machine "warmup" (@Warmup) that is followed
// by actual measurement of method execution time (@Measurement). In both cases
// ("warmup" and measurement) benchmark is repeated several times, or iterations
// (defaults to 5). Each iteration loops benchmark methods for some time (e.g.
// 1 second, as set in time and timeUnit parameters). See JMH sample
// JMHSample_20_Annotations.java for details
@Warmup(time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(time = 1, timeUnit = TimeUnit.SECONDS)
public class JmhBenchmark {

    static final int OPERATION_COUNT = 1_000;

    // Parameters allow running benchmark with different configurations.
    // This benchmark will be executed several times and class attribute
    // listSize will be set to one of provided values. See JMH sample
    // JMHSample_27_Params.java for details
    @Param({"4000", "8000", "16000", "32000"})
    public int listSize;

    ArrayList<Float> arrayList = new ArrayList<>();
    LinkedList<Float> linkedList = new LinkedList<>();
    int[] indexes = new int[OPERATION_COUNT];

    // Benchmark state can be managed in methods with @Setup (executed
    // before benchmark) or @TearDown (executed after benchmark) annotations.
    // The time of running these methods is not included in final benchmark results.
    // Level parameter specify when exactly these method will be executed:
    //   Level.Trial - before/after the whole benchmark, i.e. sequence of iterations
    //   Level.Iteration - before/after each iteration, i.e. sequence of method invocations
    //   Level.Invocation - before/after each method invocation
    // See JMH samples JMHSample_05_StateFixtures.java and JMHSample_06_FixtureLevel.java
    // for details
    @Setup(Level.Trial)
    public void generateLists() {
        Util.generateList(arrayList, listSize);
        Util.generateList(linkedList, listSize);
    }

    @Setup(Level.Iteration)
    public void generateIndexes() {
        Util.generateIndexes(indexes, listSize);
    }

    // Benchmark is the main annotation that marks methods that will have
    // execution time measured. JMH uses this and other annotations that
    // improve the reliability of benchmark results. See JMH sample
    // JMHSample_01_HelloWorld.java for details
    @Benchmark
    public void arrayListGet() {
        listGet(arrayList);
    }

    @Benchmark
    public void linkedListGet() {
        listGet(linkedList);
    }

    private void listGet(List<Float> list) {
        for (int i : indexes) {
            list.get(i);
        }
    }

    // In order to avoid the optimizing removal of code with unused results
    // (e.g. calls of method List.get(int)), results can be passed to Blackhole
    // objects that "use" them. See JMH samples JMHSample_08_DeadCode.java and
    // JMHSample_09_Blackholes.java for details
    @Benchmark
    public void arrayListGetAndConsume(Blackhole bh) {
        listGetAndConsume(arrayList, bh);
    }

    @Benchmark
    public void linkedListGetAndConsume(Blackhole bh) {
        listGetAndConsume(linkedList, bh);
    }

    private void listGetAndConsume(List<Float> list, Blackhole bh) {
        for (int i : indexes) {
            bh.consume(list.get(i));
        }
    }

    // The recommended way to run JMH benchmarks so that the results are not
    // influenced by Java IDE is to use benchmark jar file:
    //   > java -jar target/benchmarks.jar
    // During development of benchmarks for lab assignments it's more
    // convenient to use JMH Runner class and execute benchmark from IDE.
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JmhBenchmark.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
