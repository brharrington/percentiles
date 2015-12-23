set title 'All'
set term png
set logscale y 10
set output 'all-percentiles.png'
plot 'build/results/all.dat' using 1:2 title 'actual' with lines, \
     'build/results/all.dat' using 1:3 title 'buckets' with lines, \
     'build/results/all.dat' using 1:5 title 'tdigest' with lines
