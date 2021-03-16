var gulp = require('gulp');

var insert = require('gulp-insert');
var replace = require('gulp-replace');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var pipeline = require('readable-stream').pipeline;

var screeps = require('gulp-screeps');
var localConfig = require('./local.js');

/** Temporary, once JSweet get's fixed, remove this and turn tsOnly off in pom.xml
    This removes all invalid empty, non-declared root namespaces from the bundle.d.ts
    and then compiles it **/
var ts = require("gulp-typescript");
gulp.task('prepare-ts', () => {
    return gulp
        .src('target/ts/bundle.d.ts')
        .pipe(replace('console.error', 'console.log'))
        .pipe(replace(/^namespace (.*)? \{\s*?\}/gm, ''))
        .pipe(gulp.dest('target/ts'));
});
gulp.task('make-js', ['prepare-ts'], () => {
    return gulp.src(['target/ts/**/*.ts','.jsweet/candies/typings/**/*.ts'])
        .pipe(ts({
            out: 'bundle.js'
        }))
        .on('error', function (error) {
            process.exit(1);
        })
        .pipe(gulp.dest('target/js'));
});
/** End temporary **/

gulp.task('prepare-js', ['make-js'],  () => {
    return gulp

        .src(['target/js/bundle.js', 'target/candy/j4ts-0.3.1/bundle.js'])
        .pipe(insert.append('\n global.IS_SEASONAL = '+localConfig.seasonal+';'))
        .pipe(insert.append('\n if(!Memory.structures){Memory.structures={};}'))
        .pipe(insert.append('\n if(!Memory.constructionSites){Memory.constructionSites={};}'))
        .pipe(insert.append('\n ConstructionSite.prototype.memory = Memory.constructionSites[this.id];'))
        .pipe(insert.append('\n StructureExtension.prototype.memory = Memory.structures[this.id];'))
        .pipe(insert.append('\n StructureContainer.prototype.memory = Memory.structures[this.id];'))
        .pipe(insert.append('\n StructureTower.prototype.memory = Memory.structures[this.id];'))
        .pipe(insert.append('\n console.info = console.log;'))
        .pipe(concat("main.js"))
        .pipe(replace(/console.log\('EXPORT main='(.)*\s/, ''))
	    .pipe(insert.append('\n'))
        .pipe(insert.append('_exportedVar_main.init();'))
        .pipe(insert.append('\n'))
        .pipe(insert.append('module.exports.loop = function () {console.info = console.log;_exportedVar_main.loop();};'))
        .pipe(gulp.dest('target/screeps'));
});

gulp.task('compress',['make-js', 'prepare-js'], function () {
    gulp.src('src/main/js/*.js').pipe(gulp.dest(localConfig.path));
    return pipeline(
        gulp.src('target/screeps/main.js'),
        uglify(),
        gulp.dest('dist')
    );
});

gulp.task('screeps',['make-js', 'prepare-js','compress'], () => {
    if (localConfig.method == 'gulp') {
        return gulp
            .src('target/screeps/*.js')
            .pipe(screeps(localConfig.credentials));
    } else {
        return gulp
            .src('target/screeps/*.js')
            .pipe(gulp.dest(localConfig.path));
    }

});

gulp.task('default', ['make-js', 'prepare-js','compress','screeps']);