set title 'All'
set term png
set output 'all-errors.png'
plot 'build/results/all.dat' using 1:4 title 'buckets' with lines, \
     'build/results/all.dat' using 1:6 title 'tdigest' with lines
